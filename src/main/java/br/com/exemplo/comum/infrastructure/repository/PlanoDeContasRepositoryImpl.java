package br.com.exemplo.comum.infrastructure.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroPlanoDeContas;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas_;
import br.com.exemplo.comum.domain.repository.PlanoDeContasRepositoryCustom;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlanoDeContasRepositoryImpl implements PlanoDeContasRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long contaPorFiltros(FiltroPlanoDeContas filtros) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        
        Root<PlanoDeContas> root = criteria.from(PlanoDeContas.class);
        criteria.select(builder.count(root));
        
        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<PlanoDeContas> pesquisaPorFiltros(FiltroPlanoDeContas filtros, Pageable paginacao) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PlanoDeContas> criteria = builder.createQuery(PlanoDeContas.class);
        Root<PlanoDeContas> root = criteria.from(PlanoDeContas.class);

        criteria.orderBy(builder.asc(root.get(PlanoDeContas_.ID)));

        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria)
                .setFirstResult((paginacao.getPageNumber() - 1) * paginacao.getPageSize())
                .setMaxResults(paginacao.getPageSize())
                .getResultList();
    }

    private List<Predicate> aplicaFiltros(FiltroPlanoDeContas filtros, Root<PlanoDeContas> root, CriteriaBuilder builder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(filtros.nome())) {
            predicates.add(
                    builder.like(root.get(PlanoDeContas_.NOME), Utilitarios.likeFunction(filtros.nome())));
        }

        if(filtros.despesas() != null) {
            predicates.add(
                    builder.equal(root.get(PlanoDeContas_.DESPESA), filtros.despesas()));
        }

        if(filtros.pai() != null) {
            Join<PlanoDeContas, PlanoDeContas> joinPai = root.join(PlanoDeContas_.PAI);
            predicates.add(
                    builder.equal(joinPai.get(PlanoDeContas_.ID), filtros.pai()));
        } else {
            predicates.add(builder.isNull(root.get(PlanoDeContas_.PAI)));
        }

        if(filtros.ativo() != null) {
            predicates.add(
                    builder.equal(root.get(PlanoDeContas_.ATIVO), filtros.ativo()));
        }

        return predicates;
    }
}