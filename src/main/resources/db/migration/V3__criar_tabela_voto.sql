CREATE TABLE voto (
    id BIGSERIAL PRIMARY KEY,
    sessao_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    associado_cpf VARCHAR(11) NOT NULL,
    voto VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_voto_sessao FOREIGN KEY (sessao_id) REFERENCES sessao(id),
    CONSTRAINT uk_voto UNIQUE (sessao_id, associado_id)
);

CREATE INDEX idx_voto_sessao_voto ON voto(sessao_id, voto);