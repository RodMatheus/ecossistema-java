-- OPERAÇÕES LE LOG
INSERT INTO auditoria.operacao_log_auditoria(id, nome) values (1, 'Inclusão');
INSERT INTO auditoria.operacao_log_auditoria(id, nome) values (2, 'Alteração');
INSERT INTO auditoria.operacao_log_auditoria(id, nome) values (3, 'Exclusão');

-- DEPARTAMENTOS
INSERT INTO comum.departamento(nome, removido) values ('Financeiro', false);
INSERT INTO comum.departamento(nome, removido) values ('Administrativo', false);
INSERT INTO comum.departamento(nome, removido) values ('Jurídico', false);
INSERT INTO comum.departamento(nome, removido) values ('TI', false);
INSERT INTO comum.departamento(nome, removido) values ('Comercial', false);

-- BANCOS
INSERT INTO comum.banco(codigo, nome, removido) values ('001', 'Banco do Brasil S.A', false);

-- PLANOS DE CONTAS
INSERT INTO comum.plano_de_contas(nome, despesa, pai, removido) values ('Impostos', true, null, false);
INSERT INTO comum.plano_de_contas(nome, despesa, pai, removido) values ('Federal', true, 1, false);
INSERT INTO comum.plano_de_contas(nome, despesa, pai, removido) values ('INSS', true, 2, false);
INSERT INTO comum.plano_de_contas(nome, despesa, pai, removido) values ('Despesas administrativas', true, null, false);

-- CLASSIFICAÇÕES ORÇAMENTÁRIAS
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Legislativa', true, null, false);
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Ação legislativa', true, 1, false);
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Controle externo', true, 1, false);
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Judiciário', true, null, false);
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Ação judiciária', true, 4, false);
INSERT INTO comum.classificacao_orcamentaria(nome, despesa, pai, removido) values ('Administração', true, null, false);