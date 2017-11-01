package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.mapper.OperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final OperationMapper operationMapper;

    public OperationServiceImpl(OperationRepository operationRepository, OperationMapper operationMapper,
                                PositionRepository positionRepository, TransactionRepository transactionRepository,
                                InstitutionRepository institutionRepository, ChargeRepository chargeRepository) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.positionRepository = positionRepository;
        this.transactionRepository = transactionRepository;
        this.institutionRepository = institutionRepository;
        this.chargeRepository = chargeRepository;
    }

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    public OperationDTO create(OperationDTO operationDTO) {
        log.debug("Request to create Operation : {}", operationDTO);

        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);

        processOriginTransactions(operation);
        processDestinationTransactions(operation);

        return operationMapper.toDto(operation);
    }

    /**
     * Create transactions for origin position according to Operation's detail.
     *
     * @param operation containing the transaction's details.
     */
    void processOriginTransactions(final Operation operation) {
        Position origin = positionRepository.findOne(operation.getPositionFrom().getId());
        log.debug("Process origin transactions of operation {} over position {}", operation,  origin);

        final String mainTransactionDescription = join(" - ",
            operation.getOperationTypeFrom().toString(),
            institutionRepository.findOne(operation.getInstitutionFrom().getId()).getDescription());

        origin.subtract(operation.getAmountFrom());
        createTransaction(operation, origin, mainTransactionDescription, operation.getAmountFrom(), DEBIT);

        positionRepository.save(origin);

        //All charges will be applied onto the destination position

        //FIXME: Nao está aplicando taxas da origem, como withdraws.
        //FIXME: NAo está aplicando taxa por %, só FLAT_FLEE
        //FIXME: Supondo que seja BUY para o mesmo INstitution, nao pode cobrar duplicado da origem e destino
        //talvez a solucao seja ter 2 typos de operacao, uma para cada ponta
    }

    /**
     * Create transactions for destination position according to Operation's detail. It creates transactions based upon
     * the amount transferred from origin to destination and all charges (fees) applied on that.
     *
     * @param operation containing the transaction's details.
     */
    void processDestinationTransactions(final Operation operation) {
        final Position destination = positionRepository.findOne(operation.getPositionTo().getId());
        log.debug("Process destination transactions of operation {} over position {}", operation,  destination);

        final String mainTransactionDescription = join(" - ",
            operation.getOperationTypeTo().toString(),
            institutionRepository.findOne(operation.getInstitutionTo().getId()).getDescription());

        destination.add(operation.getAmountTo());
        createTransaction(operation, destination, mainTransactionDescription, operation.getAmountTo(), CREDIT);

        List<Charge> charges = getChargesToBeTransacted(operation.getInstitutionTo(),
            destination.getAsset(), operation.getOperationTypeTo());
        charges.forEach(charge -> {
            destination.subtract(charge.getAmount());
            createTransaction(operation, destination, charge.getDescription(), charge.getAmount(), DEBIT);
        });

        positionRepository.save(destination);
    }

    /**
     * Creates and persistes a new Transaction.
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
    public List<OperationDTO> findAll() {
        log.debug("Request to get all Operations");
        return operationRepository.findAll().stream()
            .map(operationMapper::toDto)
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
    public OperationDTO findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operationMapper.toDto(operation);
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
