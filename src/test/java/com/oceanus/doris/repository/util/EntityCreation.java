package com.oceanus.doris.repository.util;

import com.oceanus.doris.domain.PositionMetric;
import com.oceanus.doris.domain.Transaction;
import com.oceanus.doris.domain.enumeration.*;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Create an entity for test purpose
 *
 * This is a static class, as tests for other entities might also need it,
 * if they test an entity which requires the current entity.
 */
public class EntityCreation {

    public static class Accounts {
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

        public static com.oceanus.doris.domain.Accounts createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.Accounts createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Accounts accounts = new com.oceanus.doris.domain.Accounts()
                .description(DEFAULT_DESCRIPTION);
            // Add required entity
            accounts.setPortfolio(Portfolio.createEntity(em, true));
            // Add required entity
            accounts.getAssets().add(Asset.createEntity(em, true));
            // Add required entity
            accounts.getInstitutions().add(Institution.createEntity(em, true));

            if(flush) {
                em.persist(accounts);
                em.flush();
            }

            return accounts;
        }
    }

    public static class Asset {
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
        public static final String DEFAULT_CODE = "AAAAAAAAAA";
        public static final String DEFAULT_SYMBOL = "AAAAAAAAAA";

        public static final AssetType DEFAULT_TYPE = AssetType.COMMODITY;

        public static com.oceanus.doris.domain.Asset createEntity(EntityManager em) {
            return createEntity(em, false);
        }
        public static com.oceanus.doris.domain.Asset createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Asset asset = new com.oceanus.doris.domain.Asset()
                .description(DEFAULT_DESCRIPTION)
                .code(DEFAULT_CODE)
                .symbol(DEFAULT_SYMBOL)
                .type(DEFAULT_TYPE);

            if(flush) {
                em.persist(asset);
                em.flush();
            }

            return asset;
        }

    }

    public static class Charge {
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
        public static final ChargeType DEFAULT_CHARGE_TYPE = ChargeType.FLAT_FEE;

        public static final OperationType DEFAULT_OPERATION_TYPE = OperationType.WIRE_TRANSFER;

        public static final Double DEFAULT_AMOUNT = 1D;
        public static com.oceanus.doris.domain.Charge createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.Charge createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Charge charge = new com.oceanus.doris.domain.Charge()
                .description(DEFAULT_DESCRIPTION)
                .chargeType(DEFAULT_CHARGE_TYPE)
                .operationType(DEFAULT_OPERATION_TYPE)
                .amount(DEFAULT_AMOUNT);
            // Add required entity
            charge.setInstitution(Institution.createEntity(em, true));

            if(flush) {
                em.persist(charge);
                em.flush();
            }

            return charge;
        }

    }
    public static class Institution {



        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

        public static com.oceanus.doris.domain.Institution createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.Institution createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Institution institution = new com.oceanus.doris.domain.Institution()
                .description(DEFAULT_DESCRIPTION);

            if(flush) {
                em.persist(institution);
                em.flush();
            }

            return institution;
        }

    }

    public static class Operation {
        public static final ZonedDateTime DEFAULT_EXECUTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
        public static final Double DEFAULT_AMOUNT_FROM = 1D;
        public static final Double DEFAULT_AMOUNT_TO = 1D;
        public static final OperationType DEFAULT_OPERATION_TYPE = OperationType.WIRE_TRANSFER;

        public static com.oceanus.doris.domain.Operation createEntity(EntityManager em) {
            return createEntity(em, false);

        }

        public static com.oceanus.doris.domain.Operation createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Operation operation = new com.oceanus.doris.domain.Operation()
                .executedAt(DEFAULT_EXECUTED_AT)
                .amountFrom(DEFAULT_AMOUNT_FROM)
                .amountTo(DEFAULT_AMOUNT_TO)
                .operationTypeFrom(DEFAULT_OPERATION_TYPE)
                .operationTypeTo(DEFAULT_OPERATION_TYPE);
            // Add required entity
            operation.setPositionFrom(Position.createEntity(em, true));
            // Add required entity
            operation.setInstitutionFrom(Institution.createEntity(em, true));
            // Add required entity
            operation.setPositionTo(Position.createEntity(em, true));
            // Add required entity
            operation.setInstitutionTo(Institution.createEntity(em, true));

            if (flush) {
                em.persist(operation);
                em.flush();
            }

            return operation;
        }

    }

    public static class Portfolio {
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

        public static com.oceanus.doris.domain.Portfolio createEntity(EntityManager em) {
            return createEntity(em, false);
        }
        public static com.oceanus.doris.domain.Portfolio createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Portfolio portfolio = new com.oceanus.doris.domain.Portfolio()
                .description(DEFAULT_DESCRIPTION);

            if(flush) {
                em.persist(portfolio);
                em.flush();
            }

            return portfolio;
        }
    }

    public static class Position {
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
        public static final Double DEFAULT_BALANCE = 1D;
        public static final PositionType DEFAULT_TYPE = PositionType.LONG;
        public static final PositionStatus DEFAULT_STATUS = PositionStatus.OPEN;

        public static com.oceanus.doris.domain.Position createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.Position createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Position position = new com.oceanus.doris.domain.Position()
                .description(DEFAULT_DESCRIPTION)
                .balance(DEFAULT_BALANCE)
                .type(DEFAULT_TYPE)
                .status(DEFAULT_STATUS);
            // Add required entity
            position.setAsset(Asset.createEntity(em, true));
            // Add required entity
            position.setAccount(Accounts.createEntity(em, true));

            if (flush) {
                em.persist(position);
                em.flush();
            }

            return position;
        }
    }

    public static class PositionMetric {
        public static final Double DEFAULT_ENTRY_AVG_PRICE = 1D;
        public static final Double DEFAULT_ENTRY_AMOUNT = 1D;
        public static final Double DEFAULT_EXIT_AVG_PRICE = 1D;
        public static final Double DEFAULT_EXIT_AMOUNT = 1D;
        public static final Double DEFAULT_TX_COSTS = 1D;

        public static com.oceanus.doris.domain.PositionMetric createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.PositionMetric createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.PositionMetric positionMetric = new com.oceanus.doris.domain.PositionMetric()
                .entryAvgPrice(DEFAULT_ENTRY_AVG_PRICE)
                .entryAmount(DEFAULT_ENTRY_AMOUNT)
                .exitAvgPrice(DEFAULT_EXIT_AVG_PRICE)
                .exitAmount(DEFAULT_EXIT_AMOUNT)
                .txCosts(DEFAULT_TX_COSTS);
            // Add required entity
            positionMetric.setPosition(Position.createEntity(em, true));
            // Add required entity
            positionMetric.setAssetComparison(Asset.createEntity(em, true));

            if (flush) {
                em.persist(positionMetric);
                em.flush();
            }

            return positionMetric;
        }
    }

    public static class Transaction {
        public static final ZonedDateTime DEFAULT_EXECUTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
        public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
        public static final Double DEFAULT_AMOUNT = 1D;
        public static final TransactionType DEFAULT_TYPE = TransactionType.DEBIT;

        public static final Double DEFAULT_BALANCE = 1D;

        public static com.oceanus.doris.domain.Transaction createEntity(EntityManager em) {
            return createEntity(em, false);
        }

        public static com.oceanus.doris.domain.Transaction createEntity(EntityManager em, boolean flush) {
            com.oceanus.doris.domain.Transaction transaction = new com.oceanus.doris.domain.Transaction()
                .executedAt(DEFAULT_EXECUTED_AT)
                .description(DEFAULT_DESCRIPTION)
                .amount(DEFAULT_AMOUNT)
                .type(DEFAULT_TYPE)
                .balance(DEFAULT_BALANCE);
            // Add required entity
            transaction.setOperation(Operation.createEntity(em, true));
            // Add required entity
            transaction.setPosition(Position.createEntity(em, true));

            if (flush) {
                em.persist(transaction);
                em.flush();
            }

            return transaction;
        }
    }
}
