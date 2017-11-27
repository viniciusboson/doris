package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.*;
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
import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for the OperationService implementation service.
 *
 * @see OperationService
 */
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PositionService positionService;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private ChargeService chargeService;

    private Operation validOperation;

    @Before
    public  void setup() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository, positionService, institutionService,
            chargeService);

        validOperation = new Operation().id(1L).executedAt(ZonedDateTime.now()).amountFrom(1.0).amountTo(2.0)
            .operationTypeFrom(WITHDRAW)
            .operationTypeTo(DEPOSIT)
            .positionFrom(new Position().id(1L).description("position from").balance(0D).asset(new Asset().id(3L)))
            .positionTo(new Position().id(2L).description("position to").balance(0D).asset(new Asset().id(4L)))
            .institutionFrom(new Institution().id(1L).description("institution from"))
            .institutionTo(new Institution().id(2L).description("institution to"));
    }

    @Test
    public void processOriginTransactionsWithoutCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = origin.getBalance();

        doReturn(origin).when(positionService).findOne(origin.getId());
        doReturn(destination).when(positionService).findOne(destination.getId());
        doReturn(validOperation.getInstitutionFrom())
            .when(institutionService).findOne(validOperation.getInstitutionFrom().getId());
        doReturn(EMPTY_LIST).when(chargeService).getChargesForOperation(any(), any(), any(), any(), any(), any(), any());

        //Act
        transactionService.processOriginTransactions(validOperation);
        final Double expectedBalance = previousBalance - validOperation.getAmountFrom();

        //Assert
        assertThat(origin.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void processOriginTransactionsWithCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = origin.getBalance(), operationAmount = validOperation.getAmountFrom();
        final Double flatFee = 3.0, percentageFee = 0.0025, ignoredFee = 10.0;
        final List<Charge> charges = Arrays.asList(
            new Charge().amount(flatFee).chargeType(FLAT_FEE).target(ORIGIN),
            new Charge().amount(percentageFee).chargeType(PERCENTAGE).target(BOTH));

        doReturn(origin).when(positionService).findOne(origin.getId());
        doReturn(destination).when(positionService).findOne(destination.getId());
        doReturn(validOperation.getInstitutionFrom())
            .when(institutionService).findOne(validOperation.getInstitutionFrom().getId());
        doReturn(charges).when(chargeService).getChargesForOperation(any(), any(), any(), any(), any(), any(), any());

        //Act
        transactionService.processOriginTransactions(validOperation);
        final Double expectedBalance = previousBalance - operationAmount - flatFee  - (operationAmount * percentageFee) / 100;
        final int expectedTransactions = 1 + charges.size();

        assertThat(origin.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository, times(expectedTransactions)).save(any(Transaction.class));
    }

    @Test
    public void processDestinationTransactionsWithoutCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance();

        doReturn(origin).when(positionService).findOne(origin.getId());
        doReturn(destination).when(positionService).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionService).findOne(validOperation.getInstitutionTo().getId());
        doReturn(EMPTY_LIST).when(chargeService).getChargesForOperation(any(), any(), any(), any(), any(), any(), any());

        //Act
        transactionService.processDestinationTransactions(validOperation);
        final Double expectedBalance = previousBalance + validOperation.getAmountTo();

        //Assert
        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void processDestinationTransactionsWithCharges() {
        //Arrange
        final Position origin = validOperation.getPositionFrom();
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance(), operationAmount = validOperation.getAmountTo();
        final Double flatFee = 3.0, percentageFee = 0.0025;
        final List<Charge> charges = Arrays.asList(
            new Charge().amount(flatFee).chargeType(FLAT_FEE).target(DESTINATION),
            new Charge().amount(percentageFee).chargeType(PERCENTAGE).target(BOTH)); //ORIGIN target must be ignored

        doReturn(origin).when(positionService).findOne(origin.getId());
        doReturn(destination).when(positionService).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionService).findOne(validOperation.getInstitutionTo().getId());
        doReturn(charges).when(chargeService).getChargesForOperation(any(), any(), any(), any(), any(), any(), any());

        //Act
        transactionService.processDestinationTransactions(validOperation);
        final Double expectedBalance = previousBalance + operationAmount - flatFee  - (operationAmount * percentageFee / 100);
        final int expectedTransactions = 1 + charges.size();

        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository, times(expectedTransactions)).save(any(Transaction.class));
    }
}
