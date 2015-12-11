package ow.micropos.server.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Seat;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Modifying
    @Query(value = "ALTER TABLE sales_order ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

    List<SalesOrder> findByCustomerIsNotNull();

    List<SalesOrder> findBySeatIsNotNull();

    List<SalesOrder> findByType(SalesOrderType type);

    List<SalesOrder> findByStatus(SalesOrderStatus status);

    List<SalesOrder> findByStatusAndType(SalesOrderStatus status, SalesOrderType type);

    List<SalesOrder> findBySeat(Seat seat);

}