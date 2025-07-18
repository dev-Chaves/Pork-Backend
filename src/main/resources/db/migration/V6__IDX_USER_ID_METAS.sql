CREATE INDEX idx_metas_user_id ON tb_metas(user_id);

ALTER TABLE tb_metas ADD COLUMN data DATE;