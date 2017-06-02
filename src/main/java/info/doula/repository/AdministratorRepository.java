package info.doula.repository;

import info.doula.domain.Administrator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {

    /**
     * This method will find an Users instance in the database by its email.
     * Note that this method is not implemented and its working code will be
     * automagically generated from its signature by Spring Data JPA.
     */
    Administrator findByEmail(String email);

    @Override
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    Administrator save(Administrator administrator);

    @Query(value = "SELECT a FROM Administrator a where a.email = :username")
    Administrator findByUsername(@Param("username") String username);

    @Query(value = "SELECT a FROM Administrator a where a.email = :email and a.password = :password")
    Administrator findByEmailAndPassword(@Param("email") String email, @Param("password")String password);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Integer id);
}
