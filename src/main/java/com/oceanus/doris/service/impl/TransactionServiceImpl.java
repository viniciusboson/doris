package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.service.InstitutionService;
import com.oceanus.doris.service.PositionService;
import com.oceanus.doris.service.TransactionService;
import com.oceanus.doris.domain.Transaction;
import com.oceanus.doris.repository.TransactionRepository;
import com.oceanus.doris.service.dto.*;
import com.oceanus.doris.service.mapper.OperationMapper;
import com.oceanus.doris.service.mapper.PositionMapper;
import com.oceanus.doris.service.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.DESTINATION;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.ORIGIN;
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

    private final TransactionMapper transactionMapper;

    private final OperationMapper operationMapper;

    private final PositionMapper positionMapper;

    private final PositionService positionService;

    private final ChargeService chargeService;

    private final InstitutionService institutionService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper,
                                  OperationMapper operationMapper, PositionMapper positionMapper,
                                  PositionService positionService, ChargeService chargeService,
                                  InstitutionService institutionService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.operationMapper = operationMapper;
        this.positionMapper = positionMapper;
        this.positionService = positionService;
        this.chargeService = chargeService;
        this.institutionService = institutionService;
    }

    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {
        log.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll().stream()
            .map(transactionMapper::toDto)
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
    public TransactionDTO findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        return transactionMapper.toDto(transaction);
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
     * @param operationDTO containing the details which will generate the transactions
     */
    @Override
    public List<TransactionDTO> createTransactions(OperationDTO operationDTO) {
        log.debug("Request to create transactions for an operation: {}", operationDTO);

        final PositionDTO originDTO = positionService.findOne(operationDTO.getPositionFromId());
        final PositionDTO destinationDTO = positionService.findOne(operationDTO.getPositionToId());

        final List<Transaction> originTransactions = processOriginTransactions(operationDTO, originDTO, destinationDTO);
        final List<Transaction> destinationTransactions = processDestinationTransactions(operationDTO,
            originDTO, destinationDTO);

        return transactionMapper.toDto(
            Stream.concat(originTransactions.stream(), destinationTransactions.stream())
            .collect(Collectors.toList()));
    }

    /**
     * Create transactions for origin position according to Operation's detail.  It creates transactions based upon
     * the amount transferred from origin to destination and all charges (fees) applied on that.
     *
     * @param operationDTO containing the transaction's details.
     * @param originDTO from position
     * @param destinationDTO to position
     */
    List<Transaction> processOriginTransactions(final OperationDTO operationDTO, final PositionDTO originDTO,
                                                final PositionDTO destinationDTO) {
        log.debug("Process origin transactions of operation {} over position {}", operationDTO,  originDTO);

        final String operationTransactionDescription = join(" - ",
            operationDTO.getOperationTypeFrom().toString(),
            originDTO.getAssetCode(),
            institutionService.findOne(operationDTO.getInstitutionFromId()).getDescription());

        originDTO.decreaseBalance(operationDTO.getAmountFrom());

        final List<Transaction> createdTransactions = new ArrayList<>();
        createdTransactions.add(createTransaction(operationDTO, originDTO, operationTransactionDescription,
            operationDTO.getAmountFrom(), DEBIT));

        getChargesToBeTransacted(operationDTO, originDTO, destinationDTO, ORIGIN).stream()
            .forEach(charge -> {
                Double fee = charge.calculateFee(operationDTO.getAmountFrom());
                originDTO.decreaseBalance(fee);
                createdTransactions.add(createTransaction(operationDTO, originDTO, charge.getDescription(), fee, CHARGE));
            });

        positionService.save(originDTO);

        return createdTransactions;
    }

    /**
     * Create transactions for destination position according to Operation's detail. It creates transactions based upon
     * the amount transferred from origin to destination and all charges (fees) applied on that.
     *
     * @param operationDTO containing the transaction's details.
     * @param originDTO from position
     * @param destinationDTO to position
     */
    List<Transaction> processDestinationTransactions(final OperationDTO operationDTO, final PositionDTO originDTO,
                                                     final PositionDTO destinationDTO) {
        log.debug("Process destination transactions of operation {} over position {}", operationDTO,  destinationDTO);

        final String operationTransactionDescription = join(" - ",
            operationDTO.getOperationTypeFrom().toString(),
            originDTO.getAssetCode(),
            institutionService.findOne(operationDTO.getInstitutionToId()).getDescription());

        destinationDTO.increaseBalance(operationDTO.getAmountTo());

        final List<Transaction> createdTransactions = new ArrayList<>();
        createdTransactions.add(createTransaction(operationDTO, destinationDTO, operationTransactionDescription,
            operationDTO.getAmountTo(), CREDIT));

        getChargesToBeTransacted(operationDTO, originDTO, destinationDTO, DESTINATION).stream()
            .forEach(charge -> {
                Double fee = charge.calculateFee(operationDTO.getAmountTo());
                destinationDTO.decreaseBalance(fee);
                createdTransactions.add(createTransaction(operationDTO, destinationDTO, charge.getDescription(), fee, CHARGE));
            });

        positionService.save(destinationDTO);

        return createdTransactions;
    }

    /**
     * Creates and persists a new Transaction.
     */
    private Transaction createTransaction(OperationDTO operation, PositionDTO position, String description,
                                          Double amount, TransactionType type) {
        Transaction transaction = new Transaction()
            .executedAt(operation.getExecutedAt())
            .operation(operationMapper.toEntity(operation))
            .position(positionMapper.toEntity(position))
            .description(description)
            .amount(amount)
            .balance(position.getBalance())
            .type(type);

        transaction = transactionRepository.save(transaction);

        return transaction;
    }

    /**
     * Returns all pertinent charges for a given institution, asset and operation type.
     */
    private List<ChargeDTO> getChargesToBeTransacted(OperationDTO operationDTO, PositionDTO originDTO,
                                                     PositionDTO destinationDTO, ChargeTarget target) {
        return chargeService.getChargesForOperation(
            new InstitutionDTO().id(operationDTO.getInstitutionFromId()),
            new AssetDTO().id(originDTO.getAssetId()),
            operationDTO.getOperationTypeFrom(),
            new InstitutionDTO().id(operationDTO.getInstitutionToId()),
            new AssetDTO().id(destinationDTO.getAssetId()),
            operationDTO.getOperationTypeTo(),
            target);
    }
}
