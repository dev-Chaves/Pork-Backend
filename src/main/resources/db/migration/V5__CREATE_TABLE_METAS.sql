CREATE TABLE tb_metas(
    id BIGSERIAL PRIMARY KEY,
    meta VARCHAR(255) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_metas_user FOREIGN KEY (user_id) REFERENCES tb_usuarios(id) ON DELETE CASCADE

);

ALTER TABLE tb_usuarios ADD COLUMN investimento TEXT;
