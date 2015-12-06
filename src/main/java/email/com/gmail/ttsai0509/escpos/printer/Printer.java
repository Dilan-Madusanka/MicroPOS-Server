package email.com.gmail.ttsai0509.escpos.printer;

import java.io.IOException;

public interface Printer {

    public String getId();

    public void print(byte[] data) throws IOException;

    public void close();

}
