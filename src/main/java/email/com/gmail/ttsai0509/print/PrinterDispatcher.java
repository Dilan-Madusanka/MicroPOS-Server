package email.com.gmail.ttsai0509.print;

import java.util.List;

public interface PrinterDispatcher extends Runnable {

    List<Printer> getPrinters();

    void registerPrinter(Printer printer);

    void unregisterPrinter(Printer printer);

    boolean requestPrint(PrintJob job);

    void requestClose(boolean closePrinters);

    String getStatus();

}
