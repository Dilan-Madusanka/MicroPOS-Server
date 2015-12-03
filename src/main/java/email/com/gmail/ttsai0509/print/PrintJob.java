package email.com.gmail.ttsai0509.print;

import java.util.Date;

public class PrintJob {

    private final String target;
    private final byte[] job;
    private final Date date;

    public PrintJob(String target, byte[] job) {
        this.target = target;
        this.job = job;
        this.date = new Date();
    }

    public String getTarget() {
        return target;
    }

    public byte[] getJob() {
        return job;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Print Job : " + job.length + " bytes >> " + target + " @ " + date;
    }
}
