CREATE TABLE sessao (
    id BIGSERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    abertura TIMESTAMP NOT NULL,
    encerramento TIMESTAMP NOT NULL,

    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id),
    CONSTRAINT uk_sessao_pauta UNIQUE (pauta_id)
);
