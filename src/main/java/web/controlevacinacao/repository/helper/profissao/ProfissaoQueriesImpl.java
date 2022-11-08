package web.controlevacinacao.repository.helper.profissao;

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

import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.filter.ProfissaoFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class ProfissaoQueriesImpl implements ProfissaoQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Profissao> pesquisar(ProfissaoFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissao> criteriaQuery = builder.createQuery(Profissao.class);
		Root<Profissao> p = criteriaQuery.from(Profissao.class);
		TypedQuery<Profissao> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateList.add(builder.like(	builder.lower(p.<String>get("nome")),
										"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		predicateList.add(builder.equal(p.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(p).where(predArray);
		PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);
		typedQuery = manager.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
								
		List<Profissao> profissoes = typedQuery.getResultList();
		
		long totalProfissoes = PaginacaoUtil.getTotalRegistros(p, predArray, builder, manager);

		Page<Profissao> page = new PageImpl<>(profissoes, pageable, totalProfissoes); 
		
		return page;
	}

}
