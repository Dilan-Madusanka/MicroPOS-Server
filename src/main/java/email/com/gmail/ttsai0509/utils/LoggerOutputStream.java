package email.com.gmail.ttsai0509.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

public class LoggerOutputStream extends OutputStream {

    public enum Level {
        NONE, INFO, DEBUG, WARN, ERROR, TRACE
    }

    private final Logger logger;
    private final Level level;
    private String mem;

    public LoggerOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
        mem = "";
    }

    public LoggerOutputStream(Class<?> clazz, Level level) {
        this(LoggerFactory.getLogger(clazz), level);
    }

    @Override
    public void write(int b) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (b & 0xff);
        mem = mem + new String(bytes);

        if (mem.endsWith("\n")) {
            mem = mem.substring(0, mem.length() - 1);
            flush();
        }
    }

    @Override
    public void flush() {
        switch (level) {
            case NONE:
                break;
            case INFO:
                logger.info(mem);
                break;
            case DEBUG:
                logger.debug(mem);
                break;
            case WARN:
                logger.warn(mem);
                break;
            case ERROR:
                logger.error(mem);
                break;
            case TRACE:
                logger.trace(mem);
                break;
            default:
                logger.info(mem);
        }
        mem = "";
    }
}
