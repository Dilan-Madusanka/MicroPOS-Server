package email.com.gmail.ttsai0509.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class LoggerOutputStream extends OutputStream {

    private static final byte NEWLINE = (byte) '\n';

    public enum Level {
        NONE, INFO, DEBUG, WARN, ERROR, TRACE
    }

    private final Logger logger;
    private final Level level;
    private ByteArrayOutputStream mem;

    public LoggerOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
        this.mem = new ByteArrayOutputStream();
    }

    public LoggerOutputStream(Class<?> clazz, Level level) {
        this(LoggerFactory.getLogger(clazz), level);
    }

    @Override
    public void write(int b) {
        if (b == NEWLINE)
            flush();
        else
            mem.write(b);
    }

    @Override
    public void flush() {
        switch (level) {
            case NONE:
                break;
            case INFO:
                logger.info(mem.toString());
                break;
            case DEBUG:
                logger.debug(mem.toString());
                break;
            case WARN:
                logger.warn(mem.toString());
                break;
            case ERROR:
                logger.error(mem.toString());
                break;
            case TRACE:
                logger.trace(mem.toString());
                break;
            default:
                logger.info(mem.toString());
        }
        mem.reset();
    }
}
