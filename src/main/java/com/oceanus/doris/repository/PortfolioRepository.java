package com.oceanus.doris.repository;

import com.oceanus.doris.domain.Portfolio;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("select distinct portfolio from Portfolio portfolio left join fetch portfolio.assets left join fetch portfolio.institutions")
    List<Portfolio> findAllWithEagerRelationships();

    @Query("select portfolio from Portfolio portfolio left join fetch portfolio.assets left join fetch portfolio.institutions where portfolio.id =:id")
    Portfolio findOneWithEagerRelationships(@Param("id") Long id);

}
