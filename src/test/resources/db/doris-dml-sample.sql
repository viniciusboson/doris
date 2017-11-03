INSERT INTO INSTITUTION (ID, DESCRIPTION, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES (1951, 'Bank ABC', 'admin', current_timestamp(), 'admin', current_timestamp()),
    (1952, 'Exchange XYZ', 'admin', current_timestamp(), 'admin', current_timestamp());

INSERT INTO ASSET (ID, DESCRIPTION, CODE, SYMBOL, JHI_TYPE, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES (11051, 'Real', 'BRL', 'R$', 'CURRENCY', 'admin', current_timestamp(), 'admin', current_timestamp()),
    (11052, 'Bitcoin', 'BTC', '$', 'CURRENCY', 'admin', current_timestamp(), 'admin', current_timestamp()),
    (11053, 'Litecoin', 'LTC', '$', 'CURRENCY', 'admin', current_timestamp(), 'admin', current_timestamp());

INSERT INTO CHARGE (ID, DESCRIPTION, CHARGE_TYPE, OPERATION_TYPE, AMOUNT, TARGET, INSTITUTION_ID, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES (11101, '8 flat fee wire transfer', 'FLAT_FEE', 'WIRE_TRANSFER', 8.0, 'ORIGIN', 1951, 'admin', current_timestamp(), 'admin', current_timestamp()),
    (11102, '0.0025% transaction fee', 'PERCENTAGE', 'BUY', 0.0025, 'DESTINATION',  1952, 'admin', current_timestamp(), 'admin', current_timestamp()),
    (11103, '9.5 flat fee withdraw', 'FLAT_FEE', 'WITHDRAW', 9.5, 'DESTINATION', 1952, 'admin', current_timestamp(), 'admin', current_timestamp()),
    (11104, '1.35% withdraw fee', 'PERCENTAGE', 'WITHDRAW', 1.35, 'DESTINATION', 1952, 'admin', current_timestamp(), 'admin', current_timestamp());

INSERT INTO CHARGE_ASSET (ASSETS_ID, CHARGES_ID)
VALUES (11051,  11101), (11052, 11102), (11053, 11102), (11051, 11103), (11051, 11104);

INSERT INTO PORTFOLIO (ID, DESCRIPTION, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES ( 11151, 'Moderately Aggressive', 'admin', current_timestamp(), 'admin', current_timestamp());

INSERT INTO ACCOUNTS (ID, DESCRIPTION, PORTFOLIO_ID, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES (11201, 'Exchange XYZ Account', 11151,  'admin', current_timestamp(), 'admin', current_timestamp()),
(11202, 'Bank Account', 11151, 'admin', current_timestamp(), 'admin', current_timestamp());

INSERT INTO ACCOUNTS_ASSETS (ASSETS_ID, ACCOUNTS_ID)
VALUES (11051, 11201), (11052, 11201), (11053, 11201), (11051, 11202);

INSERT INTO ACCOUNTS_INSTITUTIONS (INSTITUTIONS_ID, ACCOUNTS_ID)
VALUES (1952, 11201), (1951, 11202);

INSERT INTO POSITION (ID, DESCRIPTION, BALANCE, JHI_TYPE, STATUS, ASSET_ID, ACCOUNT_ID, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE)
VALUES (1951, 'Bank Account - BRL - FLAT', 10000.0, 'FLAT', 'OPEN', 11051, 11202, 'admin', current_timestamp(), 'admin', current_timestamp()),
    (1952, 'Exchange - BRL - FLAT', 0.0, 'FLAT', 'OPEN', 11051, 11201, 'admin', current_timestamp(), 'admin', current_timestamp()),
    (1953, 'Exchange  - BTC - LONG', 0.0, 'LONG', 'OPEN', 11052, 11201, 'admin', current_timestamp(), 'admin', current_timestamp());
