package email.com.gmail.ttsai0509.escpos.printer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class RawPrinter implements Printer {

    private static final Logger log = LoggerFactory.getLogger(RawPrinter.class);

    private final String id;

    private final OutputStream output;

    public RawPrinter(String id, OutputStream output) {
        this.id = id;
        this.output = output;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void print(byte[] data) throws IOException {
        output.write(data);
    }

    @Override
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

}
