package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.PositionMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.BOTH;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.DESTINATION;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.ORIGIN;
import static com.oceanus.doris.domain.enumeration.ChargeType.FLAT_FEE;
import static com.oceanus.doris.domain.enumeration.TransactionType.CHARGE;
import static com.oceanus.doris.domain.enumeration.TransactionType.CREDIT;
import static com.oceanus.doris.domain.enumeration.TransactionType.DEBIT;
import static java.lang.String.join;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService{

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final PositionRepository positionRepository;

    private final TransactionRepository transactionRepository;

    private final InstitutionRepository institutionRepository;

    private final ChargeRepository chargeRepository;

    private final PositionMetricService positionMetricService;

    public OperationServiceImpl(OperationRepository operationRepository, PositionRepository positionRepository,
                                TransactionRepository transactionRepository, InstitutionRepository institutionRepository,
                                ChargeRepository chargeRepository, PositionMetricService positionMetricService) {
        this.operationRepository = operationRepository;
        this.positionRepository = positionRepository;
        this.transactionRepository = transactionRepository;
        this.institutionRepository = institutionRepository;
        this.chargeRepository = chargeRepository;
        this.positionMetricService = positionMetricService;
    }

    /**
     * Save a operation.
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    @Override
    public Operation save(Operation operation) {
        log.debug("Request to save Operation : {}", operation);
        operation = operationRepository.save(operation);
        return operation;
    }

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    public Operation create(Operation operation) {
        log.debug("Request to create Operation : {}", operation);

        operation = operationRepository.save(operation);

        final Position origin = positionRepository.findOne(operation.getPositionFrom().getId());
        final Position destination = positionRepository.findOne(operation.getPositionTo().getId());

        final List<Charge> charges = new ArrayList<>();
        charges.addAll(getChargesToBeTransacted(operation.getInstitutionFrom(), origin.getAsset(),
            operation.getOperationTypeFrom()));
        charges.addAll(getChargesToBeTransacted(operation.getInstitutionTo(), destination.getAsset(),
            operation.getOperationTypeTo()));

        final List<Transaction> originTransactions = processOriginTransactions(operation,  charges);
        final List<Transaction> destinationTransactions = processDestinationTransactions(operation, charges);

        //TODO: decouple PositionMetric with Operation using pub/sub topic
        positionMetricService.createMetric(operation);

        return operation;
    }

    /**
     * Create transactions for origin position according to Operation's detail.
     *
     * @param operation containing the transaction's details.
     */
    List<Transaction> processOriginTransactions(final Operation operation, List<Charge> charges) {
        final Position origin = positionRepository.findOne(operation.getPositionFrom().getId());
        log.debug("Process origin transactions of operation {} over position {}", operation,  origin);

        final String operationTransactionDescription = join(" - ",
            operation.getOperationTypeFrom().toString(),
            institutionRepository.findOne(operation.getInstitutionFrom().getId()).getDescription());

        final List<Transaction> createdTransactions = new ArrayList<>();

        origin.subtract(operation.getAmountFrom());
        createdTransactions.add(createTransaction(operation, origin, operationTransactionDescription,
            operation.getAmountFrom(), DEBIT));

        positionRepository.save(origin);

        charges.stream()
            .filter(charge -> charge.getTarget().equals(ORIGIN) || charge.getTarget().equals(BOTH))
            .forEach(charge -> {
                Double amount = (FLAT_FEE.equals(charge.getChargeType())?
                    charge.getAmount() : operation.getAmountFrom() * charge.getAmount() / 100);
                origin.subtract(amount);
                createdTransactions.add(createTransaction(operation, origin, charge.getDescription(), amount, CHARGE));
            });

        return createdTransactions;
    }

    /**
     * Create transactions for destination position according to Operation's detail. It creates transactions based upon
     * the amount transferred from origin to destination and all charges (fees) applied on that.
     *
     * @param operation containing the transaction's details.
     */
    List<Transaction> processDestinationTransactions(final Operation operation, List<Charge> charges) {
        final Position destination = positionRepository.findOne(operation.getPositionTo().getId());
        log.debug("Process destination transactions of operation {} over position {}", operation,  destination);

        final String chargeTransactionDescription = join(" - ",
            operation.getOperationTypeTo().toString(),
            institutionRepository.findOne(operation.getInstitutionTo().getId()).getDescription());

        final List<Transaction> createdTransactions = new ArrayList<>();

        destination.add(operation.getAmountTo());
        createdTransactions.add(createTransaction(operation, destination, chargeTransactionDescription,
            operation.getAmountTo(), CREDIT));

        charges.stream()
            .filter(charge -> charge.getTarget().equals(DESTINATION) || charge.getTarget().equals(BOTH))
            .forEach(charge -> {
                Double amount = (FLAT_FEE.equals(charge.getChargeType())?
                    charge.getAmount() : operation.getAmountTo() * charge.getAmount() / 100 );
                destination.subtract(amount);
                createdTransactions.add(createTransaction(operation, destination, charge.getDescription(), amount,
                    CHARGE));
            });

        positionRepository.save(destination);

        return createdTransactions;
    }

    /**
     * Creates and persists a new Transaction.
     */
    private Transaction createTransaction(Operation operation, Position position, String description,
                                Double amount, TransactionType type) {
        Transaction transaction = new Transaction().executedAt(operation.getExecutedAt()).operation(operation)
            .position(position).description(description).amount(amount).balance(position.getBalance()).type(type);

        transaction = transactionRepository.save(transaction);

        return transaction;
    }

    /**
     * Returns all pertinent charges for a given operation's setting
     */
    private List<Charge> getChargesToBeTransacted(Institution institution, Asset asset, OperationType operationType) {
        return chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            institution, asset, operationType);
    }

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Operation> findAll() {
        log.debug("Request to get all Operations");
        return operationRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one operation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Operation findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operation;
    }

    /**
     *  Delete the  operation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.delete(id);
    }
}
