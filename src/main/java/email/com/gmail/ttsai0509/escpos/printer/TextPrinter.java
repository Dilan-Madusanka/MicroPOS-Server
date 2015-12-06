package email.com.gmail.ttsai0509.escpos.printer;

import email.com.gmail.ttsai0509.escpos.ESCPos;

import java.io.IOException;
import java.io.PrintStream;

public class TextPrinter implements Printer {

    private final String id;
    private final PrintStream os;
    private boolean closed;

    public TextPrinter(String id, PrintStream ps) {
        this.id = id;
        this.os = ps;
        this.closed = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void print(byte[] data) throws IOException {
        if (closed)
            return;

        int i = 0;
        while (i < data.length) {

            if (i + 3 <= data.length && data[i] == 0x1B && data[i + 1] == 0x21) {
                i += 3;

            } else if (i + 3 <= data.length && data[i] == 0x1B && data[i + 1] == 0x61) {
                i += 3;

            } else if (i + 2 <= data.length && ESCPos.isCommand(ESCPos.INITIALIZE, data[i], data[i + 1])) {
                os.println("===INITIALIZED===");
                i += 2;

            } else if (i + 1 <= data.length && ESCPos.isCommand(ESCPos.FEED, data[i])) {
                os.println();
                i += 1;

            } else if (i + 3 <= data.length && ESCPos.isCommand(ESCPos.FEED_N, data[i], data[i + 1])) {
                for (int n = 0; n < data[i + 2]; n++)
                    os.println();
                i += 3;

            } else if (i + 4 <= data.length && ESCPos.isCommand(ESCPos.PART_CUT, data[i], data[i + 1], data[i + 2],
                    data[i + 3])) {
                os.println("=======CUT=======");
                i += 4;

            } else if (i + 4 <= data.length && ESCPos.isCommand(ESCPos.FULL_CUT, data[i], data[i + 1], data[i + 2],
                    data[i + 3])) {
                os.println("=======CUT=======");
                i += 4;

            } else {
                os.write(data[i]);
                i += 1;

            }

        }

    }

    @Override
    public void close() {
        this.closed = true;
    }
}
