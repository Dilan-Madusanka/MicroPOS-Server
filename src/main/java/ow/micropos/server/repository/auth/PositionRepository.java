package ow.micropos.server.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.orders.PaymentEntry;

@RepositoryRestResource(exported = false)
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Modifying
    @Query(value = "ALTER TABLE position ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

}
