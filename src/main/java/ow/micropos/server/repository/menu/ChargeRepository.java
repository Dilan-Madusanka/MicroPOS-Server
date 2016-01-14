package ow.micropos.server.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.menu.Charge;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ChargeRepository extends JpaRepository<Charge, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE charge ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE charge AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<Charge> findByArchived(boolean archived);

}

