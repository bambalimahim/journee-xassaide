package touba.xassaide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touba.xassaide.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
