package ow.micropos.server.repository.timecard;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.timecard.TimeCardEntry;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface TimeCardEntryRepository extends JpaRepository<TimeCardEntry, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE time_card_entry ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE time_card_entry AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<TimeCardEntry> findByEmployee(Employee employee);

    List<TimeCardEntry> findByDateBetween(Date start, Date end);

    List<TimeCardEntry> findByClockin(boolean clockin);

}