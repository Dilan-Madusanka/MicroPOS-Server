package ow.micropos.server.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.menu.Modifier;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ModifierRepository extends JpaRepository<Modifier, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE modifier ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE modifier AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<Modifier> findByArchived(boolean archived);

}
