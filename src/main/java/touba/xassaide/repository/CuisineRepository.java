package touba.xassaide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import touba.xassaide.domain.Cuisine;

/**
 * Spring Data SQL repository for the Cuisine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {}
