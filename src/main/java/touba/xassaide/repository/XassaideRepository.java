package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Xassaide;

/**
 * Spring Data SQL repository for the Xassaide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XassaideRepository extends JpaRepository<Xassaide, Long> {}
