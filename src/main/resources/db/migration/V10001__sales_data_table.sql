CREATE SCHEMA IF NOT EXISTS tracking;

CREATE SEQUENCE tracking.id_seq
    START WITH 100000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;

CREATE TABLE IF NOT EXISTS tracking.sales_data
(
    id                BIGINT default nextval('tracking.id_seq') NOT NULL PRIMARY KEY,
    tracking_id       VARCHAR(50)                               NOT NULL,
    visit_date        timestamp                                 NOT NULL,
    product           VARCHAR(255),
    sale_date         timestamp,
    sale_price        numeric(10, 2),
    commission_amount numeric(10, 2)
);

CREATE INDEX IF NOT EXISTS tracking_id_idx ON tracking.sales_data (tracking_id);
