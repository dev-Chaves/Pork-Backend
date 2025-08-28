ALTER TABLE tb_despesas ADD COLUMN categorias VARCHAR(50)
CREATE INDEX idx_despesas_categorias ON tb_despesas(categorias)