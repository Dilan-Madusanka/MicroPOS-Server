package ow.micropos.server.repository.records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.records.PaymentEntryRecord;

@RepositoryRestResource(exported = false)
public interface PaymentEntryRecordRepository extends JpaRepository<PaymentEntryRecord, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE payment_entry_record ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE payment_entry_record AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

}
