package web.controlevacinacao.repository.helper.lote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class LoteQueriesImpl implements LoteQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lote> pesquisar(LoteFilter filtro, Pageable pageable, boolean ehAplicacao) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lote> criteriaQuery = builder.createQuery(Lote.class);
		Root<Lote> l = criteriaQuery.from(Lote.class);
//		Join<Lote, Vacina> vacina = (Join)l.fetch("vacina", JoinType.INNER);
		l.fetch("vacina", JoinType.INNER);
		TypedQuery<Lote> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(l.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (filtro.getValidadeInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<LocalDate>get("validade"), 
		            filtro.getValidadeInicial()));
		}
		if (filtro.getValidadeFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<LocalDate>get("validade"), 
		            filtro.getValidadeFinal()));
		}
		
		if (filtro.getNroDosesDoLoteInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<Integer>get("nroDosesDoLote"), 
		            filtro.getNroDosesDoLoteInicial()));
		}
		if (filtro.getNroDosesDoLoteFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<Integer>get("nroDosesDoLote"), 
		            filtro.getNroDosesDoLoteFinal()));
		}
		
		if (filtro.getNroDosesAtualInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<Integer>get("nroDosesAtual"), 
		            filtro.getNroDosesAtualInicial()));
		}
		if (filtro.getNroDosesAtualFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<Integer>get("nroDosesAtual"), 
		            filtro.getNroDosesAtualFinal()));
		}
		
		if (filtro.getCodigoVacina() != null) {
//			predicateList.add(builder.equal(vacina.<Long>get("codigo"),
//					filtro.getCodigoVacina()));
			predicateList.add(builder.equal(l.<Vacina>get("vacina").<Long>get("codigo"),
					filtro.getCodigoVacina()));
		}
		
		predicateList.add(builder.equal(l.<Status>get("status"), 
		                 Status.ATIVO));
		
		if (ehAplicacao) {
			predicateList.add(builder.greaterThan(l.<Integer>get("nroDosesAtual"), 0));
		}
		
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(l).where(predArray);
		PaginacaoUtil.prepararOrdem(l, criteriaQuery, builder, pageable);
		typedQuery = manager.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
								
		List<Lote> lotes = typedQuery.getResultList();
		
//		long totalLotes = PaginacaoUtil.getTotalRegistros(l, predArray, builder, manager);
		long totalLotes = total(filtro, pageable);
		
		Page<Lote> page = new PageImpl<>(lotes, pageable, totalLotes); 
		
		return page;
	}
	
	private long total(LoteFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Lote> l = criteriaQuery.from(Lote.class);
		Join<Lote, Vacina> vacina = l.join("vacina", JoinType.INNER);
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(l.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (filtro.getValidadeInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<LocalDate>get("validade"), 
		            filtro.getValidadeInicial()));
		}
		if (filtro.getValidadeFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<LocalDate>get("validade"), 
		            filtro.getValidadeFinal()));
		}
		
		if (filtro.getNroDosesDoLoteInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<Integer>get("nroDosesDoLote"), 
		            filtro.getNroDosesDoLoteInicial()));
		}
		if (filtro.getNroDosesDoLoteFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<Integer>get("nroDosesDoLote"), 
		            filtro.getNroDosesDoLoteFinal()));
		}
		
		if (filtro.getNroDosesAtualInicial() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					l.<Integer>get("nroDosesAtual"), 
		            filtro.getNroDosesAtualInicial()));
		}
		if (filtro.getNroDosesAtualFinal() != null) {
			predicateList.add(builder.lessThanOrEqualTo(
					l.<Integer>get("nroDosesAtual"), 
		            filtro.getNroDosesAtualFinal()));
		}
		if (filtro.getCodigoVacina() != null) {
			predicateList.add(builder.equal(vacina.<Long>get("codigo"),
					filtro.getCodigoVacina()));
		}
		
		predicateList.add(builder.equal(l.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(builder.count(l));
		criteriaQuery.where(predArray);
		TypedQuery<Long> typedQueryTotal = manager.createQuery(criteriaQuery);
		long totalLotes = typedQueryTotal.getSingleResult();
		
		return totalLotes;
	}

}
