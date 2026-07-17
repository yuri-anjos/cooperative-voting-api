CREATE TABLE pauta (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP NOT NULL
);
