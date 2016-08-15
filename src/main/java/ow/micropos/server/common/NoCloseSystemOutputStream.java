package ow.micropos.server.common;

import java.io.IOException;
import java.io.OutputStream;

public class NoCloseSystemOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
        System.out.write(b);
    }

}
