package com.oceanus.doris.service.impl;

import com.oceanus.doris.DorisApp;
import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.mapper.OperationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oceanus.doris.domain.enumeration.OperationType.WITHDRAW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
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

    private Operation validOperation;

    @Before
    public  void setup() {
        MockitoAnnotations.initMocks(this);
        operationService = new OperationServiceImpl(operationRepository, operationMapper,
            positionRepository, transactionRepository, institutionRepository, chargeRepository);

        Position positionFrom = new Position().description("Position from");
        positionFrom.setId(1L);

        Position positionTo = new Position().description("Position to");
        positionTo.setId(2L);

        validOperation = new Operation().id(1L).executedAt(ZonedDateTime.now()).amountFrom(1D).amountTo(2D)
            .operationType(WITHDRAW)
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

        doReturn(origin).when(positionRepository).findOne(origin.getId());
        doReturn(validOperation.getInstitutionFrom())
            .when(institutionRepository).findOne(validOperation.getInstitutionFrom().getId());

        //Act
        operationService.processOriginTransactions(validOperation);
        final Double expectedBalance = previousBalance - validOperation.getAmountFrom();

        //Assert
        assertThat(origin.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }


    @Test
    public void processDestinationTransactionsWithoutCharges() {
        //Arrange
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance();

        doReturn(destination).when(positionRepository).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionRepository).findOne(validOperation.getInstitutionTo().getId());
        doReturn(Collections.EMPTY_LIST).when(chargeRepository)
            .findAllByInstitutionAndAssetsContainsAndOperationType(any(Institution.class), any(Asset.class),
                any(OperationType.class));

        //Act
        operationService.processDestinationTransactions(validOperation);
        final Double expectedBalance = previousBalance + validOperation.getAmountTo();

        //Assert
        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void processDestinationTransactionsWithCharges() {
        //Arrange
        final Position destination = validOperation.getPositionTo();
        final Double previousBalance = destination.getBalance();
        final List<Charge> charges = Arrays.asList(new Charge().amount(3D), new Charge().amount(5D));

        doReturn(destination).when(positionRepository).findOne(destination.getId());
        doReturn(validOperation.getInstitutionTo())
            .when(institutionRepository).findOne(validOperation.getInstitutionTo().getId());
        doReturn(charges).when(chargeRepository)
            .findAllByInstitutionAndAssetsContainsAndOperationType(any(Institution.class), any(Asset.class),
                any(OperationType.class));

        //Act
        operationService.processDestinationTransactions(validOperation);
        final Double expectedBalance = previousBalance + validOperation.getAmountTo() - 3D - 5D;
        final int expectedTransactions = 1 + charges.size();

        assertThat(destination.getBalance()).isEqualTo(expectedBalance);
        verify(transactionRepository, times(expectedTransactions)).save(any(Transaction.class));
    }
}
