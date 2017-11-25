package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.mapper.OperationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.BOTH;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.DESTINATION;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.ORIGIN;
import static com.oceanus.doris.domain.enumeration.ChargeType.FLAT_FEE;
import static com.oceanus.doris.domain.enumeration.ChargeType.PERCENTAGE;
import static com.oceanus.doris.domain.enumeration.OperationType.DEPOSIT;
import static com.oceanus.doris.domain.enumeration.OperationType.WITHDRAW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for the OperationService implementation service.
 *
 * @see OperationService
 */
@RunWith(SpringRunner.class)
public class OperationServiceImplTest {

    private OperationServiceImpl operationService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private OperationMapper operationMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @Mock
    private PositionMetricService positionMetricService;

    private Operation validOperation;

    @Before
    public  void setup() {
        MockitoAnnotations.initMocks(this);
        operationService = new OperationServiceImpl(operationRepository, positionRepository, transactionRepository,
            institutionRepository, chargeRepository, positionMetricService);

        Position positionFrom = new Position().description("Position from");
        positionFrom.setId(1L);

        Position positionTo = new Position().description("Position to");
        positionTo.setId(2L);

        validOperation = new Operation().id(1L).executedAt(ZonedDateTime.now()).amountFrom(1D).amountTo(2D)
            .operationTypeFrom(WITHDRAW)
            .operationTypeTo(DEPOSIT)
            .positionFrom(new Position().id(1L).description("position from").balance(0D))
            .positionTo(new Position().id(2L).description("position to").balance(0D))
            .institutionFrom(new Institution().id(1L).description("institution from"))
            .institutionTo(new Institution().id(2L).description("institution to"));
    }

    @Test
    public void processOriginTransactionsWithoutCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Double previousBalance = origin.getBalance();
        final List<Charge> charges = Collections.EMPTY_LIST;

        doReturn(origin).when(positionRepository).findOne(origin.getId());
        doReturn(validOperation.getInstitutionFrom())
            .when(institutionRepository).findOne(validOperation.getInstitutionFrom().getId());

        //Act
        operationService.processOriginTransactions(validOperation, charges);
        final Double expectedBalance = previousBalance - validOperation.getAmountFrom();

        //Assert
        assertThat(origin.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void processOriginTransactionsWithCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Double previousBalance = origin.getBalance(), operationAmount = validOperation.getAmountFrom();
        final Double flatFee = 3.0, percentageFee = 0.0025, ignoredFee = 10.0;
        final List<Charge> charges = Arrays.asList(
            new Charge().amount(flatFee).chargeType(FLAT_FEE).target(ORIGIN),
            new Charge().amount(percentageFee).chargeType(PERCENTAGE).target(BOTH),
            new Charge().amount(ignoredFee).chargeType(FLAT_FEE).target(DESTINATION)); //DESTINATION target must be ignored

        doReturn(origin).when(positionRepository).findOne(origin.getId());
        doReturn(validOperation.getInstitutionFrom())
            .when(institutionRepository).findOne(validOperation.getInstitutionFrom().getId());

        //Act
        operationService.processOriginTransactions(validOperation, charges);
        final Double expectedBalance = previousBalance - operationAmount - flatFee  - (operationAmount * percentageFee) / 100;
        final int expectedTransactions = 1 + (charges.size() - 1); //DESTINATION target must be ignored

        assertThat(origin.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository, times(expectedTransactions)).save(any(Transaction.class));
    }

    @Test
    public void processDestinationTransactionsWithoutCharges() {
        //Arrange
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance();
        final List<Charge> charges = Collections.EMPTY_LIST;

        doReturn(destination).when(positionRepository).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionRepository).findOne(validOperation.getInstitutionTo().getId());
        doReturn(Collections.EMPTY_LIST).when(chargeRepository)
            .findAllByInstitutionAndAssetsContainsAndOperationType(any(Institution.class), any(Asset.class),
                any(OperationType.class));

        //Act
        operationService.processDestinationTransactions(validOperation, charges);
        final Double expectedBalance = previousBalance + validOperation.getAmountTo();

        //Assert
        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void processDestinationTransactionsWithCharges() {
        //Arrange
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance(), operationAmount = validOperation.getAmountTo();
        final Double flatFee = 3.0, percentageFee = 0.0025, ignoredFee = 10.0;
        final List<Charge> charges = Arrays.asList(
            new Charge().amount(flatFee).chargeType(FLAT_FEE).target(DESTINATION),
            new Charge().amount(percentageFee).chargeType(PERCENTAGE).target(BOTH),
            new Charge().amount(ignoredFee).chargeType(FLAT_FEE).target(ORIGIN)); //ORIGIN target must be ignored

        doReturn(destination).when(positionRepository).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionRepository).findOne(validOperation.getInstitutionTo().getId());

        //Act
        operationService.processDestinationTransactions(validOperation, charges);
        final Double expectedBalance = previousBalance + operationAmount - flatFee  - (operationAmount * percentageFee / 100);
        final int expectedTransactions = 1 + (charges.size() - 1); //ORIGIN target must be ignored

        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository, times(expectedTransactions)).save(any(Transaction.class));
    }
}
