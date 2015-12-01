package email.com.gmail.ttsai0509.escpos;

public class PrinterDispatcherEmpty implements PrinterDispatcher {
    @Override
    public boolean print(byte[] job) {
        System.out.println("Job Request : " + job.toString());
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
