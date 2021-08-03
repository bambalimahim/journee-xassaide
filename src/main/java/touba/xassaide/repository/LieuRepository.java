package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Lieu;

/**
 * Spring Data SQL repository for the Lieu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {}
