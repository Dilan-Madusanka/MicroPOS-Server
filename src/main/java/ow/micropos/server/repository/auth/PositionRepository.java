package ow.micropos.server.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ow.micropos.server.model.auth.Position;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE position ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE position AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();


    List<Position> findByArchived(boolean archived);

}
