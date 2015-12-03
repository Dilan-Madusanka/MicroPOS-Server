package email.com.gmail.ttsai0509.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PrinterDispatcherMock implements PrinterDispatcher {

    private static final Logger log = LoggerFactory.getLogger(PrinterDispatcherMock.class);

    private final List<Printer> printers = new ArrayList<>();

    @Override
    public List<Printer> getPrinters() {
        return printers;
    }

    @Override
    public void registerPrinter(Printer printer) {
        log.info("Registered " + printer.getId());
        printers.add(printer);
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        log.info("Unregistered " + printer.getId());
        printers.remove(printer);
    }

    @Override
    public boolean requestPrint(PrintJob job) {
        log.info(job.toString());
        return false;
    }

    @Override
    public void requestClose(boolean closePrinters) {
        log.info("Request Close");
    }

    @Override
    public String getStatus() {
        return "PrinterDispatcherMock has " + printers.size() + " registered printers.";
    }

    @Override
    public void run() {
        log.info("Started");
    }
}
