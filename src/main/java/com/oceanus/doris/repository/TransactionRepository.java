package com.oceanus.doris.repository;

import com.oceanus.doris.domain.Operation;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.domain.Transaction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOperationAndPosition(Operation operation, Position position);
}
