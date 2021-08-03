package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Mouride;

/**
 * Spring Data SQL repository for the Mouride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MourideRepository extends JpaRepository<Mouride, Long> {}
