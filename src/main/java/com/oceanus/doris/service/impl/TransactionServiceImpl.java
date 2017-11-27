package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.service.InstitutionService;
import com.oceanus.doris.service.PositionService;
import com.oceanus.doris.service.TransactionService;
import com.oceanus.doris.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.BOTH;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.DESTINATION;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.ORIGIN;
import static com.oceanus.doris.domain.enumeration.ChargeType.FLAT_FEE;
import static com.oceanus.doris.domain.enumeration.TransactionType.CHARGE;
import static com.oceanus.doris.domain.enumeration.TransactionType.CREDIT;
import static com.oceanus.doris.domain.enumeration.TransactionType.DEBIT;
import static java.lang.String.join;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final PositionService positionService;

    private final InstitutionService institutionService;

    private final ChargeService chargeService;


    public TransactionServiceImpl(TransactionRepository transactionRepository, PositionService positionService,
                                  InstitutionService institutionService, ChargeService chargeService) {
        this.transactionRepository = transactionRepository;
        this.positionService = positionService;
        this.institutionService = institutionService;
        this.chargeService = chargeService;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save
     * @return the persisted entity
     */
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        transaction = transactionRepository.save(transaction);
        return transaction;
    }

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one transaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Transaction findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        return transaction;
    }

    /**
     *  Delete the  transaction by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.delete(id);
    }

    /**
     * Create transactions based on a given operation.
     *
     * @param operation containing the details which will generate the transactions
     */
    @Override
    public List<Transaction> createTransactions(Operation operation) {
        log.debug("Request to create transactions for an operation: {}", operation);

        final List<Transaction> originTransactions = processOriginTransactions(operation);
        final List<Transaction> destinationTransactions = processDestinationTransactions(operation);

        return Stream
                .concat(originTransactions.stream(), destinationTransactions.stream())
                .collect(Collectors.toList());
    }


    /**
     * Create transactions for origin position according to Operation's detail.
     *
     * @param operation containing the transaction's details.
     */
    List<Transaction> processOriginTransactions(final Operation operation) {
        log.debug("Process origin transactions of operation {}", operation);

        final Position origin = positionService.findOne(operation.getPositionFrom().getId());
        final Position destination = positionService.findOne(operation.getPositionTo().getId());

        final String transactionDescription = join(" - ",
            operation.getOperationTypeFrom().toString(),
            institutionService.findOne(operation.getInstitutionFrom().getId()).getDescription());

        origin.subtract(operation.getAmountFrom());

        final List<Transaction> createdTransactions = new ArrayList<>();
        createdTransactions.add(createTransaction(operation, origin, transactionDescription,
            operation.getAmountFrom(), DEBIT));

        getChargesToBeTransacted(operation, origin.getAsset(), destination.getAsset(), ORIGIN).stream()
            .forEach(charge -> {
                Double fee = charge.calculateFee(operation.getAmountFrom());
                origin.subtract(fee);
                createdTransactions.add(createTransaction(operation, origin, charge.getDescription(), fee, CHARGE));
            });

        positionService.save(origin);

        return createdTransactions;
    }

    /**
     * Create transactions for destination position according to Operation's detail. It creates transactions based upon
     * the amount transferred from origin to destination and all charges (fees) applied on that.
     *
     * @param operation containing the transaction's details.
     */
    List<Transaction> processDestinationTransactions(final Operation operation) {
        log.debug("Process destination transactions of operation {}", operation);

        final Position origin = positionService.findOne(operation.getPositionFrom().getId());
        final Position destination = positionService.findOne(operation.getPositionTo().getId());

        final String transactionDescription = join(" - ",
            operation.getOperationTypeTo().toString(),
            institutionService.findOne(operation.getInstitutionTo().getId()).getDescription());

        destination.add(operation.getAmountTo());

        final List<Transaction> createdTransactions = new ArrayList<>();
        createdTransactions.add(createTransaction(operation, destination, transactionDescription,
            operation.getAmountTo(), CREDIT));

        getChargesToBeTransacted(operation, origin.getAsset(), destination.getAsset(), DESTINATION).stream()
            .forEach(charge -> {
                Double fee = charge.calculateFee(operation.getAmountTo());
                destination.subtract(fee);
                createdTransactions.add(createTransaction(operation, destination, charge.getDescription(), fee,
                    CHARGE));
            });

        positionService.save(destination);

        return createdTransactions;
    }

    /**
     * Creates and persists a new Transaction.
     */
    private Transaction createTransaction(Operation operation, Position position, String description,
                                          Double amount, TransactionType type) {
        Transaction transaction = new Transaction()
            .executedAt(operation.getExecutedAt())
            .operation(operation)
            .position(position)
            .description(description)
            .amount(amount)
            .balance(position.getBalance())
            .type(type);

        return transactionRepository.save(transaction);
    }

    /**
     * Returns all pertinent charges for a given operation's setting
     */
    private List<Charge> getChargesToBeTransacted(Operation operation, Asset assetOrigin,
                                                  Asset assetDestination, ChargeTarget target) {
        return chargeService.getChargesForOperation(
            operation.getInstitutionFrom(),
            assetOrigin,
            operation.getOperationTypeFrom(),
            operation.getInstitutionTo(),
            assetDestination,
            operation.getOperationTypeTo(),
            target);
    }
}
