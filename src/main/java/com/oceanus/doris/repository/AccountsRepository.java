package com.oceanus.doris.repository;

import com.oceanus.doris.domain.Accounts;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Accounts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    @Query("select distinct accounts from Accounts accounts left join fetch accounts.assets left join fetch accounts.institutions")
    List<Accounts> findAllWithEagerRelationships();

    @Query("select accounts from Accounts accounts left join fetch accounts.assets left join fetch accounts.institutions where accounts.id =:id")
    Accounts findOneWithEagerRelationships(@Param("id") Long id);

}
