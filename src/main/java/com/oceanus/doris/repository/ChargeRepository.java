package com.oceanus.doris.repository;

import com.oceanus.doris.domain.Charge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Charge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    @Query("select distinct charge from Charge charge left join fetch charge.assets")
    List<Charge> findAllWithEagerRelationships();

    @Query("select charge from Charge charge left join fetch charge.assets where charge.id =:id")
    Charge findOneWithEagerRelationships(@Param("id") Long id);

}
