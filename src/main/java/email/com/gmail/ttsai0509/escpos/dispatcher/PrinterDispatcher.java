package email.com.gmail.ttsai0509.escpos.dispatcher;

import email.com.gmail.ttsai0509.escpos.PrintJob;
import email.com.gmail.ttsai0509.escpos.printer.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface PrinterDispatcher extends Runnable {

    static final Logger log = LoggerFactory.getLogger(PrinterDispatcher.class);

    public static PrinterDispatcher create(Class<? extends PrinterDispatcher> type, Printer... printers) {
        PrinterDispatcher dispatcher;
        try {
            dispatcher = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
            dispatcher = new PrinterDispatcherMock();
        }

        for (Printer printer : printers)
            dispatcher.registerPrinter(printer);
        return dispatcher;
    }

    List<Printer> getPrinters();

    void registerPrinter(Printer printer);

    void unregisterPrinter(Printer printer);

    boolean requestPrint(PrintJob job);

    void requestClose(boolean closePrinters);

    String getStatus();

}
