package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Drouss;

/**
 * Spring Data SQL repository for the Drouss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DroussRepository extends JpaRepository<Drouss, Long> {}
