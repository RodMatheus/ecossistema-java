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