BEGIN;

CREATE TABLE IF NOT EXISTS public.account_programs
(
    program_id serial NOT NULL,
    account_type character varying COLLATE pg_catalog."default" NOT NULL,
    program_name character varying COLLATE pg_catalog."default" NOT NULL,
    monthly_fee double precision DEFAULT 0,
    apy double precision DEFAULT 0,
    cash_back double precision DEFAULT 0,
    rewards_type character varying COLLATE pg_catalog."default",
    CONSTRAINT "AccountProgram_pkey" PRIMARY KEY (program_id)
);

CREATE TABLE IF NOT EXISTS public.accounts
(
    account_id serial NOT NULL,
    user_id integer NOT NULL,
    account_number character varying COLLATE pg_catalog."default" NOT NULL,
    account_type character varying COLLATE pg_catalog."default" NOT NULL,
	program_id integer NOT NULL,
    balance double precision NOT NULL,
    credit_limit double precision DEFAULT 0,
    amount_owed double precision DEFAULT 0,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    is_active boolean DEFAULT true,
    CONSTRAINT "Account_pkey" PRIMARY KEY (account_id),
    CONSTRAINT accounts_account_number_key UNIQUE (account_number),
	CONSTRAINT program_id FOREIGN KEY (program_id)
    REFERENCES public.account_programs (program_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION

);

CREATE TABLE IF NOT EXISTS public.transactions
(
    transaction_id serial NOT NULL,
    account_id integer NOT NULL,
    transaction_type character varying COLLATE pg_catalog."default" NOT NULL,
    amount double precision NOT NULL,
    date_time timestamp without time zone NOT NULL,
    description character varying COLLATE pg_catalog."default",
    closing_balance double precision NOT NULL,
    is_credit boolean NOT NULL,
    CONSTRAINT "Transaction_pkey" PRIMARY KEY (transaction_id)
);

ALTER TABLE IF EXISTS public.transactions
    ADD CONSTRAINT account_id FOREIGN KEY (account_id)
    REFERENCES public.accounts (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
	
-- Insert into account_programs
INSERT INTO public.account_programs (account_type, program_name, monthly_fee, apy, cash_back, rewards_type) VALUES
('CHECKING', '$0 Monthly Fee Checking', 0, 0, 0, NULL),
('CHECKING', '1% APY Checking', 5, 1, 0, NULL),
('CHECKING', 'Cash Back Checking', 10, 0, 1, 'Cash Back'),
('SAVINGS', 'Standard Savings', 0, 0.5, 0, NULL),
('SAVINGS', 'High Yield Savings', 0, 2, 0, NULL),
('CREDIT', 'Travel Rewards Credit', 0, 0, 0, 'Travel Points'),
('CREDIT', 'Cash Back Credit', 0, 0, 1.5, 'Cash Back'),
('LOAN', 'Personal Loan', 0, 0, 0, NULL),
('LOAN', 'Mortgage Loan', 0, 0, 0, NULL);
	
-- Insert into Accounts
INSERT INTO public."accounts" (account_number, user_id, account_type, program_id, balance, amount_owed, credit_limit, created_at, updated_at) 
VALUES
('938431706484704999065020441', 1, 'CHECKING', 1, 100.00, 0.0, 0.0, NOW()-INTERVAL '105 DAY', NOW()),
-- Mock checking account for user 1
('938431706484704999065020449', 1, 'SAVINGS', 4, 500.00, 0.0, 0.0, NOW()-INTERVAL '105 DAY', NOW());
-- Mock savings account for user 1

-- Insert into Transactions
-- Initial Deposit
INSERT INTO public."transactions" ("account_id", transaction_type, amount, date_time, description, closing_balance, is_credit) 
VALUES
(1, 'DEPOSIT', 100.00, NOW() - INTERVAL '105 DAY', 'Initial deposit into Checking', 100.00, false),
(2, 'DEPOSIT', 500.00, NOW() - INTERVAL '105 DAY', 'Initial deposit into Savings', 500.00, false),

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

    -- Insert fraudulent transaction
    INSERT INTO public.transactions (account_id, transaction_type, amount, date_time, description, closing_balance, is_credit)
    VALUES (1, 'WITHDRAWAL', 15.00, NOW(), 'ScamsAreMyJam.com', current_balance - 15.00, true);

    -- Update the balance after fraudulent transaction
    UPDATE public.accounts
    SET balance = balance - 15.00
    WHERE account_id = 1;
END $$;

END;