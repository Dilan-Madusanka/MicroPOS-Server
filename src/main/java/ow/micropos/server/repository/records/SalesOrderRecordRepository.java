package ow.micropos.server.repository.records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.records.SalesOrderRecord;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface SalesOrderRecordRepository extends JpaRepository<SalesOrderRecord, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE sales_order_record ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE sales_order_record AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<SalesOrderRecord> findByCustomerIsNotNull();

    List<SalesOrderRecord> findBySeatIsNotNull();

    List<SalesOrderRecord> findByType(SalesOrderType type);

    List<SalesOrderRecord> findByStatus(SalesOrderStatus status);

    List<SalesOrderRecord> findByStatusAndType(SalesOrderStatus status, SalesOrderType type);

    List<SalesOrderRecord> findByDateBetween(Date start, Date end);

}