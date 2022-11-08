package web.controlevacinacao.repository.helper.vacina;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.VacinaFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class VacinaQueriesImpl implements VacinaQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Vacina> pesquisar(VacinaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Vacina> criteriaQuery = builder.createQuery(Vacina.class);
		Root<Vacina> v = criteriaQuery.from(Vacina.class);
		TypedQuery<Vacina> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(v.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateList.add(builder.like(	builder.lower(v.<String>get("nome")),
										"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getDescricao())) {
			predicateList.add(builder.like(	builder.lower(v.<String>get("descricao")),
										"%" + filtro.getDescricao().toLowerCase() + "%"));
		}
		
		predicateList.add(builder.equal(v.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(v).where(predArray);
		PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);
		typedQuery = manager.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
								
		List<Vacina> vacinas = typedQuery.getResultList();
		
		long totalVacinas = PaginacaoUtil.getTotalRegistros(v, predArray, builder, manager);

		Page<Vacina> page = new PageImpl<>(vacinas, pageable, totalVacinas); 
		
		return page;
	}

}
