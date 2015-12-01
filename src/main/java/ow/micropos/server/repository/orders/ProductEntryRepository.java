package ow.micropos.server.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.orders.ProductEntry;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ProductEntryRepository extends JpaRepository<ProductEntry, Long> {

    @Modifying
    @Query(value = "ALTER TABLE product_entry ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

    List<ProductEntry> findByStatus(ProductEntryStatus status);

}
