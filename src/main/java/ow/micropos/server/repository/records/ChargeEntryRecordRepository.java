package ow.micropos.server.repository.records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.records.ChargeEntryRecord;
import ow.micropos.server.model.records.PaymentEntryRecord;

@RepositoryRestResource(exported = false)
public interface ChargeEntryRecordRepository extends JpaRepository<ChargeEntryRecord, Long> {

    @Modifying
    @Query(value = "ALTER TABLE charge_entry_record ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

}
