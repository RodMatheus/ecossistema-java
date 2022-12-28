package br.com.exemplo.comum.infrastructure.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroBanco;
import br.com.exemplo.comum.domain.model.entities.Banco;
import br.com.exemplo.comum.domain.model.entities.Banco_;
import br.com.exemplo.comum.domain.repository.BancoRepositoryCustom;
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
public class BancoRepositoryImpl implements BancoRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long contaPorFiltros(FiltroBanco filtros) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        
        Root<Banco> root = criteria.from(Banco.class);
        criteria.select(builder.count(root));
        
        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<Banco> pesquisaPorFiltros(FiltroBanco filtros, Pageable paginacao) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Banco> criteria = builder.createQuery(Banco.class);
        Root<Banco> root = criteria.from(Banco.class);

        criteria.orderBy(builder.asc(root.get(Banco_.codigo)));

        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria)
                .setFirstResult((paginacao.getPageNumber() - 1) * paginacao.getPageSize())
                .setMaxResults(paginacao.getPageSize())
                .getResultList();
    }

    private List<Predicate> aplicaFiltros(FiltroBanco filtros, Root<Banco> root, CriteriaBuilder builder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(filtros.nome())) {
            predicates.add(
                    builder.like(root.get(Banco_.NOME), Utilitarios.likeFunction(filtros.nome())));
        }

        if(!StringUtils.isEmpty(filtros.codigo())) {
            predicates.add(
                    builder.like(root.get(Banco_.CODIGO), filtros.codigo()));
        }

        if(filtros.ativo() != null) {
            predicates.add(
                    builder.equal(root.get(Banco_.ATIVO), filtros.ativo()));
        }

        return predicates;
    }
}