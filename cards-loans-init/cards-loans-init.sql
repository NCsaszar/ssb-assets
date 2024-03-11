BEGIN;

CREATE SEQUENCE IF NOT EXISTS user_loan_seq;

CREATE TABLE IF NOT EXISTS public.card_offer
(
    card_offer_id integer NOT NULL,
    card_offer_name character varying(255) COLLATE pg_catalog."default",
    apr double precision,
    credit_limit integer,
    card_offer_status boolean,
    CONSTRAINT card_offer_pkey PRIMARY KEY (card_offer_id)
);

CREATE TABLE IF NOT EXISTS public.card_type
(
    card_type_id serial NOT NULL,
    card_type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT card_type_pkey PRIMARY KEY (card_type_id)
);

CREATE TABLE IF NOT EXISTS public.loan
(
    loan_id integer NOT NULL,
    loan_type_id integer,
    annual_percentage_rate double precision,
    term_months integer,
    max_amount integer,
    min_amount integer,
    loan_status boolean,
    CONSTRAINT loan_pkey PRIMARY KEY (loan_id)
);

CREATE TABLE IF NOT EXISTS public.loan_type
(
    loan_type_id integer NOT NULL,
    loan_type_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT loan_type_pkey PRIMARY KEY (loan_type_id)
);

CREATE TABLE IF NOT EXISTS public.user_card
(
    card_number numeric(38, 0),
    card_status boolean NOT NULL,
    end_date timestamp(6) without time zone,
    expiration_date timestamp(6) without time zone,
    pin integer NOT NULL,
    start_date timestamp(6) without time zone,
    user_id integer NOT NULL,
    card_id integer NOT NULL,
    account_id integer,
    card_type_id integer NOT NULL,
    card_offer_id integer NOT NULL,
    CONSTRAINT user_card_pkey PRIMARY KEY (card_id)
);

CREATE TABLE IF NOT EXISTS public.user_loan
(
    user_loan_id integer NOT NULL DEFAULT nextval('user_loan_seq'::regclass),
    user_id integer NOT NULL,
    account_id integer NOT NULL,
    loan_amount double precision,
    loan_status boolean,
    loan_start_date timestamp(6) without time zone,
    loan_end_date timestamp(6) without time zone,
    loan_id integer,
    CONSTRAINT user_loan_pkey PRIMARY KEY (user_loan_id)
);

ALTER TABLE IF EXISTS public.loan
    ADD CONSTRAINT loan_loan_type_id_fkey FOREIGN KEY (loan_type_id)
    REFERENCES public.loan_type (loan_type_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.user_card
    ADD CONSTRAINT fkrrb50c95bnu37vc9d62vok00w FOREIGN KEY (card_type_id)
    REFERENCES public.card_type (card_type_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.user_card
    ADD CONSTRAINT user_card_card_offer_id_fkey FOREIGN KEY (card_offer_id)
    REFERENCES public.card_offer (card_offer_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.user_loan
    ADD CONSTRAINT user_loan_loan_id_fkey FOREIGN KEY (loan_id)
    REFERENCES public.loan (loan_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

INSERT INTO public.loan_type (loan_type_id, loan_type_name) VALUES
(1, 'Personal Loans'),
(2, 'Mortgage'),
(3, 'Student Loans'),
(4, 'Auto Loans');

INSERT INTO public.card_type (card_type_id, card_type) VALUES
(1, 'Debit'),
(2, 'Credit');


END;
