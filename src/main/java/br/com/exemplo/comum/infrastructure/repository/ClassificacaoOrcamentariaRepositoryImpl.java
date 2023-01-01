package br.com.exemplo.comum.infrastructure.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroClassificacaoOrcamentaria;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria_;
import br.com.exemplo.comum.domain.repository.ClassificacaoOrcamentariaRepositoryCustom;
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
public class ClassificacaoOrcamentariaRepositoryImpl implements ClassificacaoOrcamentariaRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long contaPorFiltros(FiltroClassificacaoOrcamentaria filtros) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        
        Root<ClassificacaoOrcamentaria> root = criteria.from(ClassificacaoOrcamentaria.class);
        criteria.select(builder.count(root));
        
        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<ClassificacaoOrcamentaria> pesquisaPorFiltros(FiltroClassificacaoOrcamentaria filtros, Pageable paginacao) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ClassificacaoOrcamentaria> criteria = builder.createQuery(ClassificacaoOrcamentaria.class);

        Root<ClassificacaoOrcamentaria> root = criteria.from(ClassificacaoOrcamentaria.class);
        criteria.orderBy(builder.asc(root.get(ClassificacaoOrcamentaria_.ID)));

        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        if(!CollectionUtils.isEmpty(predicates)) {
            criteria.where(predicates.toArray(new Predicate[0]));
        }

        return manager.createQuery(criteria).getResultList();
    }

    private List<Predicate> aplicaFiltros(FiltroClassificacaoOrcamentaria filtros,
                                          Root<ClassificacaoOrcamentaria> root, CriteriaBuilder builder) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.isNull(root.get(ClassificacaoOrcamentaria_.PAI)));

        if(!StringUtils.isEmpty(filtros.nome())) {
            predicates.add(
                    builder.like(root.get(ClassificacaoOrcamentaria_.NOME), Utilitarios.likeFunction(filtros.nome())));
        }

        if(filtros.despesas() != null) {
            predicates.add(
                    builder.equal(root.get(ClassificacaoOrcamentaria_.DESPESA), filtros.despesas()));
        }

        if(filtros.ativo() != null) {
            predicates.add(
                    builder.equal(root.get(ClassificacaoOrcamentaria_.ATIVO), filtros.ativo()));
        }

        return predicates;
    }
}