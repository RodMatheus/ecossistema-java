package br.com.exemplo.comum.infrastructure.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroCentroDeCusto;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto_;
import br.com.exemplo.comum.domain.repository.CentroDeCustoRepositoryCustom;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CentroDeCustoRepositoryImpl implements CentroDeCustoRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long contaPorFiltros(FiltroCentroDeCusto filtros) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        
        Root<CentroDeCusto> root = criteria.from(CentroDeCusto.class);
        criteria.select(builder.count(root));
        
        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<CentroDeCusto> pesquisaPorFiltros(FiltroCentroDeCusto filtros, Pageable paginacao) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<CentroDeCusto> criteria = builder.createQuery(CentroDeCusto.class);
        Root<CentroDeCusto> root = criteria.from(CentroDeCusto.class);

        criteria.orderBy(builder.asc(root.get(CentroDeCusto_.NOME)));

        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria)
                .setFirstResult((paginacao.getPageNumber() - 1) * paginacao.getPageSize())
                .setMaxResults(paginacao.getPageSize())
                .getResultList();
    }

    private List<Predicate> aplicaFiltros(FiltroCentroDeCusto filtros, Root<CentroDeCusto> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(filtros.nome())) {
            predicates.add(
                    builder.like(
                            builder.function(
                                    Utilitarios.UNACCENT_FUNCTION, String.class, builder.lower(root.get(CentroDeCusto_.NOME))),
                                    Utilitarios.likeUnaccentFunction(filtros.nome())));
        }

        if(filtros.ativo() != null) {
            predicates.add(
                    builder.equal(root.get(CentroDeCusto_.ATIVO), filtros.ativo()));
        }

        return predicates;
    }
}