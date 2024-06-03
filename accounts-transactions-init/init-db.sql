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
	closing_balance double precision NOT NULL,
    is_credit boolean NOT NULL,
    CONSTRAINT "Transaction_pkey" PRIMARY KEY (transaction_id)
);

ALTER TABLE IF EXISTS public."transactions"
    ADD CONSTRAINT account_id FOREIGN KEY (account_id)
    REFERENCES public."accounts" (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

-- Insert into Accounts
INSERT INTO public."accounts" (account_number, user_id, account_type, balance, amount_owed, credit_limit, created_at, updated_at) 
VALUES
('938431706484704999065020441', 2, 'CHECKING', 100.00, 0.0, 0.0, NOW()-INTERVAL '105 DAY', NOW()),  -- Mock checking
account for user 3
('938431706484704999065020449', 2, 'SAVINGS', 500.00, 0.0, 0.0, NOW()-INTERVAL '105 DAY', NOW());  -- Mock savings
account for user 3

-- Insert into Transactions
-- Initial Deposit
INSERT INTO public."transactions" ("account_id", transaction_type, amount, date_time, description, closing_balance, is_credit) 
VALUES
(1, 'DEPOSIT', 100.00, NOW() - INTERVAL '105 DAY', 'Initial deposit into Checking', 100.00, false),
(2, 'DEPOSIT', 500.00, NOW() - INTERVAL '105 DAY', 'Initial deposit into Savings', 500.00, false),
(2, 'WITHDRAWAL', 10.00, NOW() - INTERVAL '104 DAY', 'Standard Withdrawal', 490.00, true);

DO $$
DECLARE 
    current_balance double precision := 100.00;
    transaction_amount double precision;
    transaction_type int;
BEGIN
    FOR i IN 1..100 LOOP
        transaction_amount := (RANDOM() * 100)::decimal(10,2);
        transaction_type := (RANDOM() * 3)::int; -- Randomly select between 0, 1, and 2

        -- Check and perform transactions
        IF transaction_type = 0 THEN
            -- Deposit
            current_balance := current_balance + transaction_amount;
        ELSIF transaction_type != 0 AND current_balance >= transaction_amount THEN
            -- Withdrawal or Payment only if balance is sufficient
            current_balance := current_balance - transaction_amount;
        ELSE
            -- Skip the transaction if it would result in a negative balance
            CONTINUE;
        END IF;

        INSERT INTO public."transactions" ("account_id", transaction_type, amount, date_time, description, closing_balance, is_credit)
        VALUES (1, 
                CASE WHEN transaction_type = 0 THEN 'DEPOSIT' WHEN transaction_type = 1 THEN 'WITHDRAWAL' ELSE 'PAYMENT' END, 
                transaction_amount, 
                NOW() - (105 - i || ' DAY')::interval, 
                CASE WHEN transaction_type = 0 THEN 'Random Deposit' WHEN transaction_type = 1 THEN 'Random Withdrawal' ELSE 'Random Payment' END, 
                current_balance, 
                transaction_type != 0);
    END LOOP;

    -- Update the final balance in the accounts table
    UPDATE public."accounts"
    SET balance = current_balance
    WHERE account_id = 1;
END $$;


END;