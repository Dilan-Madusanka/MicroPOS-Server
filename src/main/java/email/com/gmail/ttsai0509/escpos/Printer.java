package email.com.gmail.ttsai0509.escpos;


import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;

public class Printer {

    public static Printer serialPrinter(String property) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException,
            IOException {

        String[] params = property.split(",");

        return new Printer(
                params[0],
                ESCPos.connectSerialPort(params[1]).getOutputStream()
        );
    }

    public static Printer cliPrinter(String property) {

        String[] params = property.split(",");

        return new Printer(
                params[0],
                System.out
        );

    }

    final String id;
    final OutputStream output;

    public Printer(String id, OutputStream output) {
        this.id = id;
        this.output = output;
    }

    public String getId() {
        return id;
    }

    public OutputStream getOutput() {
        return output;
    }
}
