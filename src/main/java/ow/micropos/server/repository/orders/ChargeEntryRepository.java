package ow.micropos.server.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;

@RepositoryRestResource(exported = false)
public interface ChargeEntryRepository extends JpaRepository<ChargeEntry, Long> {

    @Modifying
    @Query(value = "ALTER TABLE charge_entry ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

}
