package ow.micropos.server.repository.records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.records.ProductEntryRecord;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ProductEntryRecordRepository extends JpaRepository<ProductEntryRecord, Long> {

    @Modifying
    @Query(value = "ALTER TABLE product_entry_record ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

    List<ProductEntryRecord> findByStatus(ProductEntryStatus status);

}
