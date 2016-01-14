package ow.micropos.server.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.MenuItem;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    //@Query(value = "ALTER TABLE category ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    @Query(value = "ALTER TABLE category AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIds();

    List<Category> findByArchived(boolean archived);

}
