--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.0

-- Started on 2024-03-12 17:08:11

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
-- TOC entry 216 (class 1259 OID 24829)
-- Name: appointment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment (
    appointment_id integer NOT NULL,
    branch_id integer NOT NULL,
    banker_id integer NOT NULL,
    user_id integer NOT NULL,
    timeslot timestamp(6) without time zone NOT NULL,
    service_id integer,
    description character varying(255),
    date_created timestamp(6) without time zone NOT NULL,
    date_modified timestamp(6) without time zone NOT NULL,
    version integer
);


ALTER TABLE public.appointment OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24828)
-- Name: appointment_appointment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.appointment_appointment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.appointment_appointment_id_seq OWNER TO postgres;

--
-- TOC entry 4893 (class 0 OID 0)
-- Dependencies: 215
-- Name: appointment_appointment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.appointment_appointment_id_seq OWNED BY public.appointment.appointment_id;


--
-- TOC entry 218 (class 1259 OID 24838)
-- Name: banker; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.banker (
    banker_id integer NOT NULL,
    branch_id integer NOT NULL,
    timeslot_id integer,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    job_title character varying(255) NOT NULL,
    date_created timestamp(6) without time zone NOT NULL,
    date_modified timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.banker OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24837)
-- Name: banker_banker_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.banker_banker_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.banker_banker_id_seq OWNER TO postgres;

--
-- TOC entry 4894 (class 0 OID 0)
-- Dependencies: 217
-- Name: banker_banker_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.banker_banker_id_seq OWNED BY public.banker.banker_id;


--
-- TOC entry 220 (class 1259 OID 24847)
-- Name: branch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.branch (
    branch_id integer NOT NULL,
    branch_code character varying(255) NOT NULL,
    branch_name character varying(255) NOT NULL,
    branch_manager integer NOT NULL,
    phone_number character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    address1 character varying(255) NOT NULL,
    address2 character varying(255),
    city character varying(255) NOT NULL,
    state character varying(255) NOT NULL,
    postal_code character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    date_created timestamp(6) without time zone NOT NULL,
    date_modified timestamp(6) without time zone
);


ALTER TABLE public.branch OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24846)
-- Name: branch_branch_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.branch_branch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.branch_branch_id_seq OWNER TO postgres;

--
-- TOC entry 4895 (class 0 OID 0)
-- Dependencies: 219
-- Name: branch_branch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.branch_branch_id_seq OWNED BY public.branch.branch_id;


--
-- TOC entry 225 (class 1259 OID 24993)
-- Name: branch_service_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.branch_service_type (
    branch_id integer NOT NULL,
    service_id integer NOT NULL
);


ALTER TABLE public.branch_service_type OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24856)
-- Name: queue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.queue (
    queue_id integer NOT NULL,
    branch_id integer NOT NULL,
    user_id integer NOT NULL,
    checkin_time timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.queue OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24855)
-- Name: queue_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.queue_queue_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.queue_queue_id_seq OWNER TO postgres;

--
-- TOC entry 4896 (class 0 OID 0)
-- Dependencies: 221
-- Name: queue_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.queue_queue_id_seq OWNED BY public.queue.queue_id;


--
-- TOC entry 224 (class 1259 OID 24863)
-- Name: service_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_type (
    service_id integer NOT NULL,
    service_type_name character varying(255) NOT NULL,
    description character varying(255) NOT NULL
);


ALTER TABLE public.service_type OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24862)
-- Name: serviceType_service_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."serviceType_service_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."serviceType_service_id_seq" OWNER TO postgres;

--
-- TOC entry 4897 (class 0 OID 0)
-- Dependencies: 223
-- Name: serviceType_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."serviceType_service_id_seq" OWNED BY public.service_type.service_id;


--
-- TOC entry 4712 (class 2604 OID 24832)
-- Name: appointment appointment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment ALTER COLUMN appointment_id SET DEFAULT nextval('public.appointment_appointment_id_seq'::regclass);


--
-- TOC entry 4713 (class 2604 OID 24841)
-- Name: banker banker_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.banker ALTER COLUMN banker_id SET DEFAULT nextval('public.banker_banker_id_seq'::regclass);


--
-- TOC entry 4714 (class 2604 OID 24850)
-- Name: branch branch_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch ALTER COLUMN branch_id SET DEFAULT nextval('public.branch_branch_id_seq'::regclass);


--
-- TOC entry 4715 (class 2604 OID 24859)
-- Name: queue queue_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.queue ALTER COLUMN queue_id SET DEFAULT nextval('public.queue_queue_id_seq'::regclass);


--
-- TOC entry 4716 (class 2604 OID 24866)
-- Name: service_type service_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_type ALTER COLUMN service_id SET DEFAULT nextval('public."serviceType_service_id_seq"'::regclass);


--
-- TOC entry 4878 (class 0 OID 24829)
-- Dependencies: 216
-- Data for Name: appointment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appointment (appointment_id, branch_id, banker_id, user_id, timeslot, service_id, description, date_created, date_modified, version) FROM stdin;
30	1	1	1	2024-01-30 09:00:00	3	open an account	2024-02-26 20:40:59.085	2024-02-26 20:40:59.085	\N
\.


--
-- TOC entry 4880 (class 0 OID 24838)
-- Dependencies: 218
-- Data for Name: banker; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.banker (banker_id, branch_id, timeslot_id, first_name, last_name, phone_number, email, job_title, date_created, date_modified) FROM stdin;
1	1	\N	John	Bankman	111-555-0987	bankman@ssb.com	branch manager	2024-02-09 15:44:51.464	2024-02-09 15:44:51.464
2	1	\N	Sarah	Doe	111-555-2726	doe@ssb.com	associate banker	2024-02-09 15:44:51.464	2024-02-09 15:44:51.464
3	1	\N	Jim	Smith	111-555-4768	smith@ssb.com	senior banker	2024-02-09 15:44:51.464	2024-02-09 15:44:51.464
\.


--
-- TOC entry 4882 (class 0 OID 24847)
-- Dependencies: 220
-- Data for Name: branch; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.branch (branch_id, branch_code, branch_name, branch_manager, phone_number, email, address1, address2, city, state, postal_code, country, lat, lng, date_created, date_modified) FROM stdin;
13	SSB006	6th Secure Sentinel Bank - New Orleans	111	123-456-7890	norleans@securesentinelbank.com	123 Main St	apt. 3	Anytown	Anystate	12345	USA	29.9546	-90.075	2024-02-09 15:39:07.699	2024-02-09 15:39:07.699
14	SSB007	7th Secure Sentinel Bank - Seattle	555	555 555 5555	seattle@securesentinelbank.com	555 Party st	\N	Westville	new 555	55555	USA	47.6062	-122.3321	2024-02-09 15:44:51.464	2024-02-09 15:44:51.464
15	SSB008	8th Secure Sentinel Bank - Detroit	777	333 333 3333	detroit@securesentinelbank.com	77 7th Street		Rock City	East Rock	37281	USA	42.3314	-83.0457	2024-02-13 16:17:16.792	2024-02-13 16:17:16.793
1	SSB001	1st Secure Sentinel Bank - Houston	123	111-222-3333	houston@securesentinelbank.com	102 West Ave	Suite 102	Houston	Texas	12121	USA	29.7632	-95.3632	2024-01-26 11:26:37.239	2024-01-26 11:26:37.239
9	SSB002	2nd Secure Sentinel Bank - N.Y.C.	234	212-555-1234	nyc@securesentinelbank.com	123 Wall Street	\N	New York	NY	10005	USA	40.7074	-74.0113	2024-02-22 10:26:52.088	2024-02-22 10:26:52.088
10	SSB003	3rd Secure Sentinel Bank - L.A.	456	222-333-4444	la@securesentinelbank.com	789 Pine St	Apt 202	Cityville	Los Santos	67890	USA	34.0522	-118.2436	2024-01-26 11:26:37.239	2024-01-26 11:26:37.239
11	SSB004	4th Secure Sentinel Bank - Boston	789	333-444-5555	boston@securesentinelbank.com	123 Elm St	Unit 303	Beantown	North Clover	12345	USA	42.3611	-71.057	2024-01-26 11:26:37.239	2024-01-26 11:26:37.239
12	SSB005	5th Secure Sentinel Bank - Chicago	444	444 444 4444	chicago@securesentinelbank.com	555 North St	\N	North Testota	Illinois	88888	USA	41.8781	87.6298	2024-02-09 15:07:24.605	2024-02-09 15:07:24.605
\.


--
-- TOC entry 4887 (class 0 OID 24993)
-- Dependencies: 225
-- Data for Name: branch_service_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.branch_service_type (branch_id, service_id) FROM stdin;
\.


--
-- TOC entry 4884 (class 0 OID 24856)
-- Dependencies: 222
-- Data for Name: queue; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.queue (queue_id, branch_id, user_id, checkin_time) FROM stdin;
\.


--
-- TOC entry 4886 (class 0 OID 24863)
-- Dependencies: 224
-- Data for Name: service_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_type (service_id, service_type_name, description) FROM stdin;
2	Loan Approval	Get approval for a bank loan.
3	Account Opening	Open a new bank account.
1	General	Nothing specified.
4	Card Application	Apply for a card at the bank.
\.


--
-- TOC entry 4898 (class 0 OID 0)
-- Dependencies: 215
-- Name: appointment_appointment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.appointment_appointment_id_seq', 30, true);


--
-- TOC entry 4899 (class 0 OID 0)
-- Dependencies: 217
-- Name: banker_banker_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.banker_banker_id_seq', 1, false);


--
-- TOC entry 4900 (class 0 OID 0)
-- Dependencies: 219
-- Name: branch_branch_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.branch_branch_id_seq', 16, true);


--
-- TOC entry 4901 (class 0 OID 0)
-- Dependencies: 221
-- Name: queue_queue_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.queue_queue_id_seq', 1, false);


--
-- TOC entry 4902 (class 0 OID 0)
-- Dependencies: 223
-- Name: serviceType_service_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."serviceType_service_id_seq"', 1, false);


--
-- TOC entry 4718 (class 2606 OID 24836)
-- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (appointment_id);


--
-- TOC entry 4720 (class 2606 OID 24845)
-- Name: banker banker_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.banker
    ADD CONSTRAINT banker_pkey PRIMARY KEY (banker_id);


--
-- TOC entry 4722 (class 2606 OID 24854)
-- Name: branch branch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (branch_id);


--
-- TOC entry 4724 (class 2606 OID 24861)
-- Name: queue queue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (queue_id);


--
-- TOC entry 4726 (class 2606 OID 24870)
-- Name: service_type serviceType_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_type
    ADD CONSTRAINT "serviceType_pkey" PRIMARY KEY (service_id);


--
-- TOC entry 4727 (class 2606 OID 24871)
-- Name: appointment appointment_banker_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_banker_id_fkey FOREIGN KEY (banker_id) REFERENCES public.banker(banker_id) NOT VALID;


--
-- TOC entry 4728 (class 2606 OID 24876)
-- Name: appointment appointment_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


--
-- TOC entry 4729 (class 2606 OID 24881)
-- Name: appointment appointment_service_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_service_id_fkey FOREIGN KEY (service_id) REFERENCES public.service_type(service_id) NOT VALID;


--
-- TOC entry 4730 (class 2606 OID 24886)
-- Name: banker banker_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.banker
    ADD CONSTRAINT banker_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


--
-- TOC entry 4732 (class 2606 OID 25008)
-- Name: branch_service_type fkde570frlgduk6smb1kqfjsntr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch_service_type
    ADD CONSTRAINT fkde570frlgduk6smb1kqfjsntr FOREIGN KEY (service_id) REFERENCES public.service_type(service_id);


--
-- TOC entry 4733 (class 2606 OID 25013)
-- Name: branch_service_type fkh58fdd8a4we6veaxu9sx0lqh5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch_service_type
    ADD CONSTRAINT fkh58fdd8a4we6veaxu9sx0lqh5 FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id);


--
-- TOC entry 4731 (class 2606 OID 24891)
-- Name: queue queue_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.queue
    ADD CONSTRAINT queue_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(branch_id) NOT VALID;


-- Completed on 2024-03-12 17:08:12

--
-- PostgreSQL database dump complete
--

