--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.0

-- Started on 2024-03-05 16:04:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 24600)
-- Name: appointment; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.appointment (
    appointment_id integer NOT NULL,
    banker_id integer,
    branch_id integer,
    date_created timestamp(6) without time zone,
    date_modified timestamp(6) without time zone,
    description character varying(255),
    service_id integer,
    timeslot timestamp(6) without time zone,
    user_id integer,
    version integer
);


ALTER TABLE public.appointment OWNER TO branchservice;

--
-- TOC entry 215 (class 1259 OID 24599)
-- Name: appointment_appointment_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public.appointment_appointment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.appointment_appointment_id_seq OWNER TO branchservice;

--
-- TOC entry 4352 (class 0 OID 0)
-- Dependencies: 215
-- Name: appointment_appointment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public.appointment_appointment_id_seq OWNED BY public.appointment.appointment_id;


--
-- TOC entry 218 (class 1259 OID 24607)
-- Name: banker; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.banker (
    banker_id integer NOT NULL,
    branch_id integer,
    date_created timestamp(6) without time zone,
    date_modified timestamp(6) without time zone,
    email character varying(255),
    first_name character varying(255),
    job_title character varying(255),
    last_name character varying(255),
    phone_number character varying(255)
);


ALTER TABLE public.banker OWNER TO branchservice;

--
-- TOC entry 217 (class 1259 OID 24606)
-- Name: banker_banker_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public.banker_banker_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.banker_banker_id_seq OWNER TO branchservice;

--
-- TOC entry 4353 (class 0 OID 0)
-- Dependencies: 217
-- Name: banker_banker_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public.banker_banker_id_seq OWNED BY public.banker.banker_id;


--
-- TOC entry 220 (class 1259 OID 24616)
-- Name: branch; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.branch (
    branch_id integer NOT NULL,
    address1 character varying(255),
    address2 character varying(255),
    branch_code character varying(255),
    branch_manager integer,
    branch_name character varying(255),
    city character varying(255),
    country character varying(255),
    date_created timestamp(6) without time zone,
    date_modified timestamp(6) without time zone,
    email character varying(255),
    lat double precision,
    lng double precision,
    phone_number character varying(255),
    postal_code character varying(255),
    state character varying(255)
);


ALTER TABLE public.branch OWNER TO branchservice;

--
-- TOC entry 219 (class 1259 OID 24615)
-- Name: branch_branch_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public.branch_branch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.branch_branch_id_seq OWNER TO branchservice;

--
-- TOC entry 4354 (class 0 OID 0)
-- Dependencies: 219
-- Name: branch_branch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public.branch_branch_id_seq OWNED BY public.branch.branch_id;


--
-- TOC entry 221 (class 1259 OID 24624)
-- Name: branch_service_type; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.branch_service_type (
    branch_id integer NOT NULL,
    service_id integer NOT NULL
);


ALTER TABLE public.branch_service_type OWNER TO branchservice;

--
-- TOC entry 223 (class 1259 OID 24628)
-- Name: queue; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.queue (
    queue_id integer NOT NULL,
    branch_id integer,
    checkin_time timestamp(6) without time zone,
    user_id integer
);


ALTER TABLE public.queue OWNER TO branchservice;

--
-- TOC entry 222 (class 1259 OID 24627)
-- Name: queue_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public.queue_queue_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.queue_queue_id_seq OWNER TO branchservice;

--
-- TOC entry 4355 (class 0 OID 0)
-- Dependencies: 222
-- Name: queue_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public.queue_queue_id_seq OWNED BY public.queue.queue_id;


--
-- TOC entry 225 (class 1259 OID 24635)
-- Name: service_type; Type: TABLE; Schema: public; Owner: branchservice
--

CREATE TABLE public.service_type (
    service_id integer NOT NULL,
    description character varying(255),
    service_type_name character varying(255)
);


ALTER TABLE public.service_type OWNER TO branchservice;

--
-- TOC entry 226 (class 1259 OID 24678)
-- Name: serviceType_service_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public."serviceType_service_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."serviceType_service_id_seq" OWNER TO branchservice;

--
-- TOC entry 4356 (class 0 OID 0)
-- Dependencies: 226
-- Name: serviceType_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public."serviceType_service_id_seq" OWNED BY public.service_type.service_id;


--
-- TOC entry 224 (class 1259 OID 24634)
-- Name: service_type_service_id_seq; Type: SEQUENCE; Schema: public; Owner: branchservice
--

CREATE SEQUENCE public.service_type_service_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.service_type_service_id_seq OWNER TO branchservice;

--
-- TOC entry 4357 (class 0 OID 0)
-- Dependencies: 224
-- Name: service_type_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: branchservice
--

ALTER SEQUENCE public.service_type_service_id_seq OWNED BY public.service_type.service_id;


--
-- TOC entry 4165 (class 2604 OID 24679)
-- Name: appointment appointment_id; Type: DEFAULT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment ALTER COLUMN appointment_id SET DEFAULT nextval('public.appointment_appointment_id_seq'::regclass);


--
-- TOC entry 4166 (class 2604 OID 24680)
-- Name: banker banker_id; Type: DEFAULT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.banker ALTER COLUMN banker_id SET DEFAULT nextval('public.banker_banker_id_seq'::regclass);


--
-- TOC entry 4167 (class 2604 OID 24681)
-- Name: branch branch_id; Type: DEFAULT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.branch ALTER COLUMN branch_id SET DEFAULT nextval('public.branch_branch_id_seq'::regclass);


--
-- TOC entry 4168 (class 2604 OID 24682)
-- Name: queue queue_id; Type: DEFAULT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.queue ALTER COLUMN queue_id SET DEFAULT nextval('public.queue_queue_id_seq'::regclass);


--
-- TOC entry 4169 (class 2604 OID 24683)
-- Name: service_type service_id; Type: DEFAULT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.service_type ALTER COLUMN service_id SET DEFAULT nextval('public."serviceType_service_id_seq"'::regclass);


--
-- TOC entry 4336 (class 0 OID 24600)
-- Dependencies: 216
-- Data for Name: appointment; Type: TABLE DATA; Schema: public; Owner: branchservice
--

INSERT INTO public.appointment (appointment_id, banker_id, branch_id, date_created, date_modified, description, service_id, timeslot, user_id, version) VALUES (31, 2, 1, '2024-02-28 16:24:46.651', '2024-02-28 16:24:46.651', 'loan information
', 1, '2024-02-28 08:00:00', 1, NULL);
INSERT INTO public.appointment (appointment_id, banker_id, branch_id, date_created, date_modified, description, service_id, timeslot, user_id, version) VALUES (32, 2, 1, '2024-02-28 17:57:29.802', '2024-02-28 17:57:29.802', 'saying hi', 1, '2024-02-28 10:00:00', 1, NULL);
INSERT INTO public.appointment (appointment_id, banker_id, branch_id, date_created, date_modified, description, service_id, timeslot, user_id, version) VALUES (33, 2, 1, '2024-02-29 10:00:49.387', '2024-02-29 10:00:49.387', 'financial advice', 1, '2024-02-29 14:00:00', 1, 0);
INSERT INTO public.appointment (appointment_id, banker_id, branch_id, date_created, date_modified, description, service_id, timeslot, user_id, version) VALUES (34, 2, 1, '2024-02-29 11:11:16.635', '2024-02-29 11:11:16.635', 'discuss loan options', 2, '2024-02-29 09:00:00', 1, 0);
INSERT INTO public.appointment (appointment_id, banker_id, branch_id, date_created, date_modified, description, service_id, timeslot, user_id, version) VALUES (35, 2, 1, '2024-02-29 13:14:05.528', '2024-02-29 13:14:05.528', 'talk about new loan', 2, '2024-02-29 08:00:00', 1, 0);


--
-- TOC entry 4338 (class 0 OID 24607)
-- Dependencies: 218
-- Data for Name: banker; Type: TABLE DATA; Schema: public; Owner: branchservice
--

INSERT INTO public.banker (banker_id, branch_id, date_created, date_modified, email, first_name, job_title, last_name, phone_number) VALUES (1, 1, '2024-02-09 15:39:07.699', '2024-02-09 15:39:07.699', 'ssb1@test.com', 'Linda', 'Branch Manager', 'Bankman', '111-555-7675');
INSERT INTO public.banker (banker_id, branch_id, date_created, date_modified, email, first_name, job_title, last_name, phone_number) VALUES (2, 1, '2024-02-09 15:39:07.699', '2024-02-09 15:39:07.699', 'ssb2@test.com', 'Lucas', 'Senior Banker', 'Smith', '111-555-7672');
INSERT INTO public.banker (banker_id, branch_id, date_created, date_modified, email, first_name, job_title, last_name, phone_number) VALUES (3, 1, '2024-02-09 15:39:07.699', '2024-02-09 15:39:07.699', 'ssb3@test.com', 'Michael', 'Associate Banker', 'Fletcher', '111-555-7678');


--
-- TOC entry 4340 (class 0 OID 24616)
-- Dependencies: 220
-- Data for Name: branch; Type: TABLE DATA; Schema: public; Owner: branchservice
--

INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (13, '123 Main St', 'apt. 3', 'SSB006', 111, '6th Secure Sentinel Bank - New Orleans', 'Anytown', 'USA', '2024-02-09 15:39:07.699', '2024-02-09 15:39:07.699', 'norleans@securesentinelbank.com', 29.9546, -90.075, '123-456-7890', '12345', 'Anystate');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (14, '555 Party st', NULL, 'SSB007', 555, '7th Secure Sentinel Bank - Seattle', 'Westville', 'USA', '2024-02-09 15:44:51.464', '2024-02-09 15:44:51.464', 'seattle@securesentinelbank.com', 47.6062, -122.3321, '555 555 5555', '55555', 'new 555');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (15, '77 7th Street', '', 'SSB008', 777, '8th Secure Sentinel Bank - Detroit', 'Rock City', 'USA', '2024-02-13 16:17:16.792', '2024-02-13 16:17:16.793', 'detroit@securesentinelbank.com', 42.3314, -83.0457, '333 333 3333', '37281', 'East Rock');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (1, '102 West Ave', 'Suite 102', 'SSB001', 123, '1st Secure Sentinel Bank - Houston', 'Houston', 'USA', '2024-01-26 11:26:37.239', '2024-01-26 11:26:37.239', 'houston@securesentinelbank.com', 29.7632, -95.3632, '111-222-3333', '12121', 'Texas');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (9, '123 Wall Street', NULL, 'SSB002', 234, '2nd Secure Sentinel Bank - N.Y.C.', 'New York', 'USA', '2024-02-22 10:26:52.088', '2024-02-22 10:26:52.088', 'nyc@securesentinelbank.com', 40.7074, -74.0113, '212-555-1234', '10005', 'NY');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (10, '789 Pine St', 'Apt 202', 'SSB003', 456, '3rd Secure Sentinel Bank - L.A.', 'Cityville', 'USA', '2024-01-26 11:26:37.239', '2024-01-26 11:26:37.239', 'la@securesentinelbank.com', 34.0522, -118.2436, '222-333-4444', '67890', 'Los Santos');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (11, '123 Elm St', 'Unit 303', 'SSB004', 789, '4th Secure Sentinel Bank - Boston', 'Beantown', 'USA', '2024-01-26 11:26:37.239', '2024-01-26 11:26:37.239', 'boston@securesentinelbank.com', 42.3611, -71.057, '333-444-5555', '12345', 'North Clover');
INSERT INTO public.branch (branch_id, address1, address2, branch_code, branch_manager, branch_name, city, country, date_created, date_modified, email, lat, lng, phone_number, postal_code, state) VALUES (12, '555 North St', NULL, 'SSB005', 444, '5th Secure Sentinel Bank - Chicago', 'North Testota', 'USA', '2024-02-09 15:07:24.605', '2024-02-09 15:07:24.605', 'chicago@securesentinelbank.com', 41.8781, -87.6298, '444 444 4444', '88888', 'Illinois');


--
-- TOC entry 4341 (class 0 OID 24624)
-- Dependencies: 221
-- Data for Name: branch_service_type; Type: TABLE DATA; Schema: public; Owner: branchservice
--



--
-- TOC entry 4343 (class 0 OID 24628)
-- Dependencies: 223
-- Data for Name: queue; Type: TABLE DATA; Schema: public; Owner: branchservice
--



--
-- TOC entry 4345 (class 0 OID 24635)
-- Dependencies: 225
-- Data for Name: service_type; Type: TABLE DATA; Schema: public; Owner: branchservice
--

INSERT INTO public.service_type (service_id, description, service_type_name) VALUES (2, 'Get approval for a bank loan.', 'Loan Approval');
INSERT INTO public.service_type (service_id, description, service_type_name) VALUES (3, 'Open a new bank account.', 'Account Opening');
INSERT INTO public.service_type (service_id, description, service_type_name) VALUES (1, 'Nothing specified.', 'General');
INSERT INTO public.service_type (service_id, description, service_type_name) VALUES (4, 'Apply for a card at the bank.', 'Card Application');


--
-- TOC entry 4358 (class 0 OID 0)
-- Dependencies: 215
-- Name: appointment_appointment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public.appointment_appointment_id_seq', 35, true);


--
-- TOC entry 4359 (class 0 OID 0)
-- Dependencies: 217
-- Name: banker_banker_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public.banker_banker_id_seq', 1, false);


--
-- TOC entry 4360 (class 0 OID 0)
-- Dependencies: 219
-- Name: branch_branch_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public.branch_branch_id_seq', 16, true);


--
-- TOC entry 4361 (class 0 OID 0)
-- Dependencies: 222
-- Name: queue_queue_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public.queue_queue_id_seq', 1, false);


--
-- TOC entry 4362 (class 0 OID 0)
-- Dependencies: 226
-- Name: serviceType_service_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public."serviceType_service_id_seq"', 1, false);


--
-- TOC entry 4363 (class 0 OID 0)
-- Dependencies: 224
-- Name: service_type_service_id_seq; Type: SEQUENCE SET; Schema: public; Owner: branchservice
--

SELECT pg_catalog.setval('public.service_type_service_id_seq', 1, false);


--
-- TOC entry 4171 (class 2606 OID 24605)
-- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (appointment_id);


--
-- TOC entry 4173 (class 2606 OID 24614)
-- Name: banker banker_pkey; Type: CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.banker
    ADD CONSTRAINT banker_pkey PRIMARY KEY (banker_id);


--
-- TOC entry 4175 (class 2606 OID 24623)
-- Name: branch branch_pkey; Type: CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (branch_id);


--
-- TOC entry 4177 (class 2606 OID 24633)
-- Name: queue queue_pkey; Type: CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (queue_id);


--
-- TOC entry 4179 (class 2606 OID 24642)
-- Name: service_type service_type_pkey; Type: CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.service_type
    ADD CONSTRAINT service_type_pkey PRIMARY KEY (service_id);


--
-- TOC entry 4180 (class 2606 OID 24684)
-- Name: appointment appointment_banker_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_banker_id_fkey FOREIGN KEY (banker_id) REFERENCES public.banker(banker_id) NOT VALID;


--
-- TOC entry 4181 (class 2606 OID 24689)
-- Name: appointment appointment_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


--
-- TOC entry 4182 (class 2606 OID 24694)
-- Name: appointment appointment_service_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_service_id_fkey FOREIGN KEY (service_id) REFERENCES public.service_type(service_id) NOT VALID;


--
-- TOC entry 4186 (class 2606 OID 24699)
-- Name: banker banker_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.banker
    ADD CONSTRAINT banker_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


--
-- TOC entry 4183 (class 2606 OID 24643)
-- Name: appointment fk51qqg1kgyewjwwikeorl0b76g; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk51qqg1kgyewjwwikeorl0b76g FOREIGN KEY (banker_id) REFERENCES public.banker(banker_id);


--
-- TOC entry 4188 (class 2606 OID 24663)
-- Name: branch_service_type fkde570frlgduk6smb1kqfjsntr; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.branch_service_type
    ADD CONSTRAINT fkde570frlgduk6smb1kqfjsntr FOREIGN KEY (service_id) REFERENCES public.service_type(service_id);


--
-- TOC entry 4189 (class 2606 OID 24668)
-- Name: branch_service_type fkh58fdd8a4we6veaxu9sx0lqh5; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.branch_service_type
    ADD CONSTRAINT fkh58fdd8a4we6veaxu9sx0lqh5 FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id);


--
-- TOC entry 4184 (class 2606 OID 24653)
-- Name: appointment fkhpvvt0bbwxfofh8dbw8n7nd5g; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fkhpvvt0bbwxfofh8dbw8n7nd5g FOREIGN KEY (service_id) REFERENCES public.service_type(service_id);


--
-- TOC entry 4187 (class 2606 OID 24658)
-- Name: banker fki2wkmt1mh6wkmxrrt4k6hmkhs; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.banker
    ADD CONSTRAINT fki2wkmt1mh6wkmxrrt4k6hmkhs FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id);


--
-- TOC entry 4185 (class 2606 OID 24648)
-- Name: appointment fkirq7r526btqxyk1gsuq4wa2h3; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fkirq7r526btqxyk1gsuq4wa2h3 FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id);


--
-- TOC entry 4190 (class 2606 OID 24673)
-- Name: queue fkk95iy8klj8l3p2tpnxy91u9u0; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.queue
    ADD CONSTRAINT fkk95iy8klj8l3p2tpnxy91u9u0 FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id);


--
-- TOC entry 4191 (class 2606 OID 24704)
-- Name: queue queue_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: branchservice
--

ALTER TABLE ONLY public.queue
    ADD CONSTRAINT queue_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


-- Completed on 2024-03-05 16:04:21

--
-- PostgreSQL database dump complete
--

