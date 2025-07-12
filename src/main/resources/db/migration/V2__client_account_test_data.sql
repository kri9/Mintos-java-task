INSERT INTO client (name)
VALUES ('Alice');
INSERT INTO client (name)
VALUES ('Bob');
INSERT INTO client (name)
VALUES ('Charlie');

INSERT INTO account (client_id, currency, balance)
VALUES (1, 'USD', 1000.00);
INSERT INTO account (client_id, currency, balance)
VALUES (1, 'EUR', 500.00);
INSERT INTO account (client_id, currency, balance)
VALUES (2, 'USD', 200.00);
INSERT INTO account (client_id, currency, balance)
VALUES (2, 'GBP', 300.00);
INSERT INTO account (client_id, currency, balance)
VALUES (3, 'EUR', 150.00);

-- Alice (USD) → Bob (USD), same currency transfer
INSERT INTO transaction (source_account_id,
                         destination_account_id,
                         amount,
                         source_currency,
                         destination_currency,
                         exchange_rate,
                         description)
VALUES (1, 3, 150.00, 'USD', 'USD', 1.00000000, 'Gift to Bob');

-- Alice (EUR) → Charlie (EUR), same currency transfer
INSERT INTO transaction (source_account_id,
                         destination_account_id,
                         amount,
                         source_currency,
                         destination_currency,
                         exchange_rate,
                         description)
VALUES (2, 5, 50.00, 'EUR', 'EUR', 1.00000000, 'Repayment');

-- Bob (GBP) → Alice (USD), cross currency transfer
INSERT INTO transaction (source_account_id,
                         destination_account_id,
                         amount,
                         source_currency,
                         destination_currency,
                         exchange_rate,
                         description)
VALUES (4, 1, 100.00, 'GBP', 'USD', 1.25000000, 'Business payment');

-- Charlie (EUR) → Bob (GBP), cross currency transfer
INSERT INTO transaction (source_account_id,
                         destination_account_id,
                         amount,
                         source_currency,
                         destination_currency,
                         exchange_rate,
                         description)
VALUES (5, 4, 75.00, 'EUR', 'GBP', 0.86000000, 'Currency exchange');

-- Alice internal transfer from USD to EUR
INSERT INTO transaction (source_account_id,
                         destination_account_id,
                         amount,
                         source_currency,
                         destination_currency,
                         exchange_rate,
                         description)
VALUES (1, 2, 200.00, 'USD', 'EUR', 0.92000000, 'Internal conversion');