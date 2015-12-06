package email.com.gmail.ttsai0509.escpos.dispatcher;

import email.com.gmail.ttsai0509.escpos.PrintJob;
import email.com.gmail.ttsai0509.escpos.printer.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PrinterDispatcherAsync implements PrinterDispatcher {

    private static final Logger log = LoggerFactory.getLogger(PrinterDispatcherAsync.class);

    private final AtomicBoolean closeRequest, closeRequestPrinters;
    private final AtomicInteger jobRejected, jobTakeInterrupt,
            jobRequest, jobRequestPass, jobRequestFail,
            jobProcess, jobProcessPass, jobProcessFail;

    private Thread currentThread;
    private final ConcurrentHashMap<String, Printer> printerMap;
    private final LinkedBlockingQueue<PrintJob> printJobs;

    public PrinterDispatcherAsync() {

        closeRequest = new AtomicBoolean(false);
        closeRequestPrinters = new AtomicBoolean(false);
        jobRequest = new AtomicInteger(0);
        jobRequestPass = new AtomicInteger(0);
        jobRequestFail = new AtomicInteger(0);
        jobProcess = new AtomicInteger(0);
        jobProcessPass = new AtomicInteger(0);
        jobProcessFail = new AtomicInteger(0);
        jobRejected = new AtomicInteger(0);
        jobTakeInterrupt = new AtomicInteger(0);

        printerMap = new ConcurrentHashMap<>();
        printJobs = new LinkedBlockingQueue<>();
    }

    @Override
    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(new ArrayList<>(printerMap.values()));
    }

    @Override
    public void registerPrinter(Printer printer) {
        log.debug("Registering printer " + printer.toString());
        printerMap.put(printer.getId(), printer);
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        log.debug("Unregister printer " + printer.toString());
        printerMap.remove(printer.getId());
    }

    @Override
    public boolean requestPrint(PrintJob job) {

        if (closeRequest.get()) {

            jobRejected.incrementAndGet();
            return false;

        } else {

            jobRequest.incrementAndGet();

            if (!printerMap.containsKey(job.getTarget())) {
                jobRequestFail.incrementAndGet();
                return false;
            }

            try {
                printJobs.put(job);
                jobRequestPass.incrementAndGet();
                return true;

            } catch (InterruptedException e) {
                jobRequestFail.incrementAndGet();
                return false;
            }

        }
    }

    @Override
    public void requestClose(boolean closePrinters) {
        closeRequest.set(true);
        closeRequestPrinters.set(closePrinters);
        currentThread.interrupt();
    }

    @Override
    public void run() {

        currentThread = Thread.currentThread();

        while (!closeRequest.get() || !printJobs.isEmpty()) {

            try {
                PrintJob job = printJobs.take();

                jobProcess.incrementAndGet();

                if (!printerMap.containsKey(job.getTarget())) {

                    jobProcessFail.incrementAndGet();

                } else {

                    try {
                        printerMap
                                .get(job.getTarget())
                                .print(job.getJob());
                        jobProcessPass.incrementAndGet();
                    } catch (IOException e) {
                        jobProcessFail.incrementAndGet();
                    }

                }

            } catch (InterruptedException e) {
                jobTakeInterrupt.incrementAndGet();
            }

        }

        log.info("Closing Async Print Dispatcher.");

        if (closeRequestPrinters.get())
            printerMap.values().forEach(Printer::close);

    }

    @Override
    public String getStatus() {
        return "Printer Dispatcher Status" +
                "\n=====================" +
                "\nJobs Requested : " + jobRequest.get() +
                "\n   Passed      : " + jobRequestPass.get() +
                "\n   Failed      : " + jobRequestFail.get() +
                "\nJobs Processed : " + jobProcess.get() +
                "\n   Passed      : " + jobProcessPass.get() +
                "\n   Failed      : " + jobProcessFail.get() +
                "\nJobs Rejected  : " + jobRejected.get() +
                "\nTake Interrupt : " + jobTakeInterrupt.get() +
                "\n=====================";
    }

}
