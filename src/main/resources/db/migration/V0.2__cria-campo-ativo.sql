ALTER TABLE comum.banco ADD COLUMN ativo boolean not null default true;
ALTER TABLE comum.centro_de_custo ADD COLUMN ativo boolean not null default true;
ALTER TABLE comum.departamento ADD COLUMN ativo boolean not null default true;
ALTER TABLE comum.projeto ADD COLUMN ativo boolean not null default true;
ALTER TABLE comum.plano_de_contas ADD COLUMN ativo boolean not null default true;
ALTER TABLE comum.classificacao_orcamentaria ADD COLUMN ativo boolean not null default true;