CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE card_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT true
);

CREATE TABLE cards
(
    id               BIGINT PRIMARY KEY,
    card_number      VARCHAR(255),
    card_holder_name VARCHAR(255),
    expiry_date      DATE,
    status           VARCHAR(50),
    balance          DECIMAL(19, 2),
    user_id          BIGINT NOT NULL,
    CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE tokens
(
    id      BIGINT PRIMARY KEY,
    token   VARCHAR(255),
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_cards_card_number ON cards (card_number);
CREATE INDEX idx_cards_user_id ON cards (user_id);
CREATE INDEX idx_tokens_token ON tokens (token);