package email.com.gmail.ttsai0509.print;

import email.com.gmail.ttsai0509.escpos.ESCPos;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class Printer {

    private static final Logger log = LoggerFactory.getLogger(Printer.class);

    public static Printer serialPrinter(String name, String port) {
        try {
            return new Printer(name, ESCPos.connectSerialPort(port).getOutputStream());
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("Unable to create serial printer '" + name + "'. Defaulting to a cli printer.");
            return new Printer(name, System.out);
        }
    }

    public static Printer cliPrinter(String name) {
        return new Printer(name, System.out);
    }

    public static Printer streamPrinter(String name, OutputStream out) {
        return new Printer(name, out);
    }

    private final String id;
    private final OutputStream output;

    public Printer(String id, OutputStream output) {
        this.id = id;
        this.output = output;
    }

    public String getId() {
        return id;
    }

    public void close() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("Printer " + id + " could not be safely closed.");
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    @Override
    public String toString() {
        return "Printer " + id + " using output " + output.getClass();
    }

    OutputStream getOutput() {
        return output;
    }

}
