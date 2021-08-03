package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Daara;

/**
 * Spring Data SQL repository for the Daara entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DaaraRepository extends JpaRepository<Daara, Long> {}
