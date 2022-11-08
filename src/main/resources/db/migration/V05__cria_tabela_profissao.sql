CREATE TABLE public.profissao
(
    codigo serial NOT NULL,
    nome text,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);
