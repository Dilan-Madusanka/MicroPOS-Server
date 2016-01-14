package ow.micropos.server.repository.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.target.Seat;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE seat ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE seat AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<Seat> findByArchived(boolean archived);

}
