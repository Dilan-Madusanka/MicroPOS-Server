package ow.micropos.server.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InvalidParameterException;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.timecard.TimeCardEntry;
import ow.micropos.server.repository.employee.EmployeeRepository;
import ow.micropos.server.repository.timecard.TimeCardEntryRepository;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TimeCardService {


    private static final Logger log = LoggerFactory.getLogger(TimeCardService.class);
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm");

    @Autowired EmployeeRepository employeeRepository;
    @Autowired TimeCardEntryRepository tceRepo;

    @Value("${micropos.timecard.folder}")
    private String folder;

    @Transactional(readOnly = true)
    public List<TimeCardEntry> getTimeCardEntries() {

        return tceRepo.findAll();

    }

    @Transactional(readOnly = true)
    public List<TimeCardEntry> getTimeCardEntries(Date start, Date end) {

        return tceRepo.findByDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<TimeCardEntry> getTimeCardEntries(Long id, Date start, Date end) {

        return tceRepo.findByDateBetween(start, end)
                .stream()
                .filter(tce -> Objects.equals(tce.getEmployee().getId(), id))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<TimeCardEntry> getTimeCardEntries(Long id) {

        Employee e = employeeRepository.findOne(id);

        return tceRepo.findByEmployee(e)
                .stream()
                .filter(tce -> !tce.isArchived())
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = false)
    public TimeCardEntry standardEntry(Employee employee, String img, boolean clockin) {

        TimeCardEntry tceLast = getLastEntry(employee);

        if ((tceLast == null || !tceLast.isClockin()) && !clockin) {
            throw new MicroPosException("Already clocked OUT.");
        } else if (tceLast != null && tceLast.isClockin() && clockin) {
            throw new MicroPosException("Already clocked IN.");
        }

        TimeCardEntry tce = TimeCardEntry.employeeEntry(employee, clockin);

        tceRepo.save(tce);

        if (img != null && !img.isEmpty()) {
            String filename = folder
                    + employee.getLastName() + "_"
                    + employee.getFirstName() + "_"
                    + dateTimeFormat.format(tce.getDate()) + "_"
                    + (clockin ? "in" : "out") + ".jpg";

            try (OutputStream stream = new FileOutputStream(filename)) {
                byte[] data = Base64.decodeBase64(img.getBytes());
                stream.write(data);
            } catch (Exception e) {
                log.warn("Error saving snapshot " + filename);
            }

        }

        return tce;

    }

    @Transactional(readOnly = false)
    public TimeCardEntry verifiedEntry(Employee employee, boolean clockin, Date date, Employee verifier) {

        TimeCardEntry tce = TimeCardEntry.verifiedEntry(employee, verifier, date, clockin);

        return tceRepo.save(tce);

    }

    @Transactional(readOnly = false)
    public TimeCardEntry updateEntry(long tceId, Date date, Employee verifier) {

        TimeCardEntry tce = tceRepo.findOne(tceId);

        if (tce == null)
            throw new InvalidParameterException("TimeCardEntry doesn't exist.");

        tce.setDate(date);

        tce.setVerifier(verifier);

        return tceRepo.save(tce);

    }

    @Transactional(readOnly = false)
    public boolean archiveEntry(long tceId, Employee verifier) {

        TimeCardEntry tce = tceRepo.findOne(tceId);

        if (tce == null)
            return false;

        tce.setArchived(true);

        tce.setArchiveDate(new Date());

        tce.setVerifier(verifier);

        tceRepo.save(tce);

        return true;

    }

    private TimeCardEntry getLastEntry(Employee employee) {

        List<TimeCardEntry> tcEntries = tceRepo.findByEmployee(employee);

        tcEntries.sort((tce1, tce2) -> tce1.getDate().compareTo(tce2.getDate()));

        if (tcEntries.isEmpty())
            return null;
        else
            return tcEntries.get(tcEntries.size() - 1);

    }

}
