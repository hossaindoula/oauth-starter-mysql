package info.doula.repository;

import info.doula.domain.ClientDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ClientDetailsRepository extends CrudRepository<ClientDetails, String> {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(String id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void save(String id);
}
