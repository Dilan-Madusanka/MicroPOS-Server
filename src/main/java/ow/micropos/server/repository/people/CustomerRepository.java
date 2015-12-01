package ow.micropos.server.repository.people;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ow.micropos.server.model.people.Customer;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query(value = "ALTER TABLE customer ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetIds();

    List<Customer> findByArchived(boolean archived);

}
