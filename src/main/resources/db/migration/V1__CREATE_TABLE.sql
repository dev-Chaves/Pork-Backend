CREATE TABLE tb_usuarios(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    verificado BOOLEAN DEFAULT FALSE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_verification_tokens(
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expira_em TIMESTAMP NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES tb_usuarios(id) ON DELETE CASCADE
);

CREATE TABLE tb_despesas(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    receita DECIMAL(15,2) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    categoria VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_receita_positive CHECK (receita > 0),
    CONSTRAINT chk_valor_positive CHECK (valor > 0),
    CONSTRAINT fk_despesas_user FOREIGN KEY (user_id) REFERENCES tb_usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_despesas_user_id ON tb_despesas(user_id);
CREATE INDEX idx_despesas_categoria ON tb_despesas(categoria);
CREATE INDEX idx_verification_tokens_token ON tb_verification_tokens(token);
CREATE INDEX idx_verification_tokens_user_id ON tb_verification_tokens(user_id);
