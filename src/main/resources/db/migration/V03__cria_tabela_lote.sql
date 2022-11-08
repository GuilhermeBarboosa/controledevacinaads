CREATE TABLE public.lote
(
    codigo bigserial NOT NULL,
    validade date,
    nro_doses_do_lote integer,
    nro_doses_atual integer,
    codigo_vacina bigint,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);
