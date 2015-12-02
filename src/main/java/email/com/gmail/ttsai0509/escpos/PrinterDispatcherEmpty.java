package email.com.gmail.ttsai0509.escpos;

import java.util.Arrays;

public class PrinterDispatcherEmpty implements PrinterDispatcher {

    @Override
    public void registerPrinter(Printer printer) {
        System.out.println("Registered " + printer.getId());
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        System.out.println("Unregistered " + printer.getId());
    }

    @Override
    public boolean requestPrint(String id, byte[] job) {
        System.out.println("Printer " + id + " : " + Arrays.toString(job));
        return false;
    }

    @Override
    public void requestClose() {
        System.out.println("Request Close");
    }

    @Override
    public void printStatus() {
        System.out.println("Status N/A");
    }

    @Override
    public void run() {
        System.out.println("Started");
    }
}
