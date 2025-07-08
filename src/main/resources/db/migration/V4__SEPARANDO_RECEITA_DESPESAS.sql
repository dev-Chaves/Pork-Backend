ALTER TABLE tb_despesas DROP COLUMN receita;

ALTER TABLE tb_usuarios ADD receita DECIMAL(15,2) DEFAULT 0.00;