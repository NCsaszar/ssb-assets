BEGIN;
DROP TABLE IF EXISTS public."transactions";
DROP TABLE IF EXISTS public."accounts";

CREATE TABLE IF NOT EXISTS public."accounts"
(
    account_id serial NOT NULL,
    user_id integer NOT NULL,
	account_number character varying NOT NULL UNIQUE,
     account_type character varying NOT NULL,
    balance double precision NOT NULL,
	credit_limit double precision DEFAULT 0,
    amount_owed double precision DEFAULT 0,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
	is_active boolean DEFAULT true,
    CONSTRAINT "Account_pkey" PRIMARY KEY (account_id)
);

CREATE TABLE IF NOT EXISTS public."transactions"
(
    transaction_id serial NOT NULL,
    account_id integer NOT NULL,
    transaction_type character varying NOT NULL,
    amount double precision NOT NULL,
    date_time timestamp without time zone NOT NULL,
    description character varying,
    CONSTRAINT "Transaction_pkey" PRIMARY KEY (transaction_id)
);

ALTER TABLE IF EXISTS public."transactions"
    ADD CONSTRAINT account_id FOREIGN KEY (account_id)
    REFERENCES public."accounts" (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

-- Insert into Accounts
INSERT INTO public."accounts" (account_number, user_id, account_type, balance, amount_owed, credit_limit, created_at, updated_at) VALUES
('123456789012', 1, 'CHECKING', 5000.00, 0.0, 0.0, NOW(), NOW()),  -- Mock checking account for user 1
('123456789013', 1, 'CHECKING', 10000.00, 0.0, 0.0, NOW(), NOW()), -- Mock checking account for user 1
('123456789014', 1, 'SAVINGS', 100000.00, 0.0, 0.0, NOW(), NOW()), -- Mock savings account for user 1
('123456789015', 2, 'SAVINGS', 8000.00, 0.0, 0.0, NOW(), NOW()),  -- Mock savings account for user 2
('123456789016', 3, 'CREDIT', 15000.00, 0.0, 15000.00, NOW(), NOW()),  -- Mock credit account for user 3
('123456789017', 299, 'CHECKING', 1000.00, 0.0, 0.0, NOW(), NOW()),  -- Mock checking account for user 299
('123456789018', 299, 'SAVINGS', 500.00, 0.0, 0.0, NOW(), NOW()),  -- Mock savings account for user 299
('123456789019', 299, 'CREDIT', 15000.00, 0.0, 15000.00, NOW(), NOW());  -- Mock credit account for user 299


-- Insert into Transactions
INSERT INTO public."transactions" ("account_id", transaction_type, amount, date_time, description) VALUES
(1, 'DEPOSIT', 500.00, NOW(), 'Initial deposit into Checking'),       -- Deposit for Checking account
(1, 'DEPOSIT', 800.00, NOW(), 'Deposit into Checking'),       -- Deposit for Checking account
(2, 'DEPOSIT', 800.00, NOW(), 'Deposit into Checking'),       -- Deposit for Checking account
(4, 'DEPOSIT', 800.00, NOW(), 'Initial deposit into Savings'),        -- Deposit for Savings account
(5, 'PAYMENT', 150.00, NOW(), 'Credit Card Payment');                -- Payment for Credit account  

END;