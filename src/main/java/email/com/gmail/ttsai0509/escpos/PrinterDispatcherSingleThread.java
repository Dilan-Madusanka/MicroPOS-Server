package email.com.gmail.ttsai0509.escpos;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PrinterDispatcherSingleThread implements PrinterDispatcher {

    private AtomicBoolean closeRequest;
    private AtomicInteger jobRejected, jobTakeInterrupt,
            jobRequest, jobRequestPass, jobRequestFail,
            jobProcess, jobProcessPass, jobProcessFail;

    private Thread currentThread;

    private final ConcurrentHashMap<String, Printer> printerMap;
    private final LinkedBlockingQueue<PrintJob> printJobs;

    public PrinterDispatcherSingleThread() throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {

        closeRequest = new AtomicBoolean(false);
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
    public void registerPrinter(Printer printer) {
        printerMap.put(printer.getId(), printer);
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        printerMap.remove(printer.getId());
    }

    @Override
    public boolean requestPrint(String id, byte[] job) {

        if (closeRequest.get()) {

            jobRejected.incrementAndGet();
            return false;

        } else {

            jobRequest.incrementAndGet();

            if (!printerMap.containsKey(id)) {
                jobRequestFail.incrementAndGet();
                return false;
            }

            try {
                printJobs.put(new PrintJob(id, job));
                jobRequestPass.incrementAndGet();
                return true;

            } catch (InterruptedException e) {
                jobRequestFail.incrementAndGet();
                return false;
            }

        }
    }

    @Override
    public void requestClose() {
        closeRequest.set(true);
        currentThread.interrupt();
    }

    @Override
    public void run() {

        currentThread = Thread.currentThread();

        while (!closeRequest.get() || !printJobs.isEmpty()) {

            try {
                PrintJob job = printJobs.take();

                jobProcess.incrementAndGet();

                if (!printerMap.containsKey(job.getId())) {

                    jobProcessFail.incrementAndGet();

                } else {

                    try {
                        printerMap
                                .get(job.getId())
                                .getOutput()
                                .write(job.getJob());
                        jobProcessPass.incrementAndGet();
                    } catch (IOException e) {
                        jobProcessFail.incrementAndGet();
                    }

                }

            } catch (InterruptedException e) {
                jobTakeInterrupt.incrementAndGet();
            }

        }
    }

    @Override
    public void printStatus() {
        System.out.println("\n=====================" +
                "\nJobs Requested : " + jobRequest.get() +
                "\n   Passed      : " + jobRequestPass.get() +
                "\n   Failed      : " + jobRequestFail.get() +
                "\nJobs Processed : " + jobProcess.get() +
                "\n   Passed      : " + jobProcessPass.get() +
                "\n   Failed      : " + jobProcessFail.get() +
                "\nJobs Rejected  : " + jobRejected.get() +
                "\nTake Interrupt : " + jobTakeInterrupt.get() +
                "\n=====================");
    }

    private static class PrintJob {
        private final String id;
        private final byte[] job;

        public PrintJob(String id, byte[] job) {
            this.id = id;
            this.job = job;
        }

        public String getId() {
            return id;
        }

        public byte[] getJob() {
            return job;
        }
    }

}
