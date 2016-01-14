package ow.micropos.server.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.orders.PaymentEntry;

@RepositoryRestResource(exported = false)
public interface PaymentEntryRepository extends JpaRepository<PaymentEntry, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE payment_entry ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE payment_entry AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

}
