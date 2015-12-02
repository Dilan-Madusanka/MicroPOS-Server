package email.com.gmail.ttsai0509.escpos;

public interface PrinterDispatcher extends Runnable {

    void registerPrinter(Printer printer);

    void unregisterPrinter(Printer printer);

    boolean requestPrint(String id, byte[] job);

    void requestClose();

    void printStatus();

}
