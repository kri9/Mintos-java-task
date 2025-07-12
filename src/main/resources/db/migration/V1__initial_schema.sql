CREATE TABLE client
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE account
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT         NOT NULL,
    currency  VARCHAR(3)        NOT NULL,
    balance   DECIMAL(18, 2) NOT NULL CHECK (balance >= 0),
    FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE TABLE transaction
(
    id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_account_id      BIGINT,
    destination_account_id BIGINT,
    timestamp              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount                 DECIMAL(18, 2) NOT NULL, -- Amount transferred in source account's currency
    source_currency        VARCHAR(3)        NOT NULL,
    destination_currency   VARCHAR(3)        NOT NULL,
    exchange_rate          DECIMAL(18, 8) NOT NULL ,
    description            VARCHAR(255),
    FOREIGN KEY (source_account_id) REFERENCES account (id),
    FOREIGN KEY (destination_account_id) REFERENCES account (id)
);