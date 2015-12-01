package email.com.gmail.ttsai0509.escpos;

public interface PrinterDispatcher extends Runnable {

    boolean print(byte[] job);

    void requestClose();

    void printStatus();

}
