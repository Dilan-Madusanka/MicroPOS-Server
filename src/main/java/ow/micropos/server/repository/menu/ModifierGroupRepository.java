package ow.micropos.server.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.menu.ModifierGroup;

@RepositoryRestResource(exported = false)
public interface ModifierGroupRepository extends JpaRepository<ModifierGroup, Long> {

    @Modifying
    @Query(value = "ALTER TABLE modifier_group ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

}
