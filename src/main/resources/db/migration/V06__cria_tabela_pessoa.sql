CREATE TABLE public.pessoa
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    data_nascimento date,
    codigo_profissao integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

ALTER TABLE public.pessoa
    ADD FOREIGN KEY (codigo_profissao)
    REFERENCES public.profissao (codigo)
    NOT VALID;
