package ow.micropos.server.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.menu.MenuItem;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE menu_item ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE menu_item AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<MenuItem> findByArchived(boolean archived);

}
