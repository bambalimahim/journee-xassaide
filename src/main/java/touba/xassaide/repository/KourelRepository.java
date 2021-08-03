package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Kourel;

/**
 * Spring Data SQL repository for the Kourel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KourelRepository extends JpaRepository<Kourel, Long> {}
