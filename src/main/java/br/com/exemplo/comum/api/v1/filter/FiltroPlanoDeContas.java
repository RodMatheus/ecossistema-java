package br.com.exemplo.comum.api.v1.filter;

public record FiltroPlanoDeContas(String nome, Long pai, Boolean despesas, Boolean ativo) { }