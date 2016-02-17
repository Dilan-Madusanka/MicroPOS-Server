package ow.micropos.server.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackupTask {

    private static final Logger log = LoggerFactory.getLogger(BackupTask.class);
    private static final DateFormat backupDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String command = "cmd /c mysqldump -u %s -p%s %s > %s";

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${micropos.backup.folder}")
    private String folder;

    @Value("${micropos.backup.prefix}")
    private String prefix;

    @Value("${micropos.backup.database}")
    private String database;

    @Value("${micropos.backup.history}")
    private int history;

    @Value("${micropos.backup.enabled}")
    private boolean enabled;

    @Scheduled(cron = "${micropos.backup.cron}")
    @Transactional(readOnly = false)
    public void backupDatabaseToFile() {

        if (!enabled)
            return;

        try {

            Date date = new Date();

            Runtime rt = Runtime.getRuntime();

            String file = folder + prefix + backupDateFormat.format(date) + "_" + date.getTime() + ".sql";

            Process pr = rt.exec(String.format(command, username, password, database, file));

            int processComplete = pr.waitFor();

            if (processComplete == 0) {
                log.info("Backup Success - " + file);
                tryClearLRU(folder);
            } else {
                log.warn("Backup Failed - " + processComplete);
            }

        } catch (Exception e) {

            log.warn("Backup Failed - " + e.getMessage());

        }
    }

    private void tryClearLRU(String folderPath) {

        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            log.warn("Check backup directory (not directory) : " + folderPath);
            return;
        }

        File[] filesArr = folder.listFiles();
        if (filesArr == null) {
            log.warn("Check backup directory (no files) : " + folderPath);
            return;
        }

        List<File> files = Arrays.asList(filesArr)
                .stream()
                .filter(f -> f.getName().startsWith(prefix))
                .sorted((f, g) -> f.getName().compareTo(g.getName()))
                .collect(Collectors.toList());

        while (files.size() > history) {
            if (files.get(0).delete()) {
                files.remove(0);
            } else {
                log.warn("Unable to delete file " + files.get(0).getName());
                return;
            }
        }

    }
}
