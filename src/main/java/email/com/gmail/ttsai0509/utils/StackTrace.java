package email.com.gmail.ttsai0509.utils;

public final class StackTrace {

    private StackTrace() {
        throw new RuntimeException(this.getClass().getSimpleName() + " is a utility class.");
    }

    public static String current(boolean verbose) {
        return verbose ?
                Thread.currentThread().getStackTrace()[2].toString() :
                Thread.currentThread().getStackTrace()[2].getMethodName();
    }

}
