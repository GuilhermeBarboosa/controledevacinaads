package web.controlevacinacao.repository.helper.aplicacao;

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
import org.springframework.util.StringUtils;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.AplicacaoFilter;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.model.filter.PessoaFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class AplicacaoQueriesImpl implements AplicacaoQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Aplicacao> pesquisar(AplicacaoFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Aplicacao> criteriaQuery = builder.createQuery(Aplicacao.class);
		Root<Aplicacao> p = criteriaQuery.from(Aplicacao.class);
		p.fetch("lote", JoinType.INNER);
		p.fetch("pessoa", JoinType.INNER);
		TypedQuery<Aplicacao> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (filtro.getData() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					p.<LocalDate>get("data"), 
		            filtro.getData()));
		}
		if (filtro.getCodigoLote() != null) {
			predicateList.add(builder.equal(p.<Lote>get("lote").<Long>get("codigo"),
					filtro.getCodigoLote()));
		}
		if (filtro.getCodigoPessoa() != null) {
			predicateList.add(builder.equal(p.<Pessoa>get("pessoa").<Long>get("codigo"),
					filtro.getCodigoPessoa()));
		}
		
		predicateList.add(builder.equal(p.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(p).where(predArray);
		PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);
		typedQuery = manager.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
								
		List<Aplicacao> aplicacoes = typedQuery.getResultList();
		
		long totalAplicacoes = total(filtro, pageable);
		
		Page<Aplicacao> page = new PageImpl<>(aplicacoes, pageable, totalAplicacoes); 
		
		return page;
	}
	
	private long total(AplicacaoFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Aplicacao> p = criteriaQuery.from(Aplicacao.class);
		Join<Aplicacao, Lote> Lote = p.join("lote", JoinType.INNER);
		Join<Aplicacao, Pessoa> Pessoa = p.join("pessoa", JoinType.INNER);
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (filtro.getData() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					p.<LocalDate>get("data"), 
		            filtro.getData()));
		}
		if (filtro.getCodigoLote() != null) {
			predicateList.add(builder.equal(p.<Lote>get("lote").<Long>get("codigo"),
					filtro.getCodigoLote()));
		}
		if (filtro.getCodigoPessoa() != null) {
			predicateList.add(builder.equal(p.<Pessoa>get("pessoa").<Long>get("codigo"),
					filtro.getCodigoPessoa()));
		}
		
		predicateList.add(builder.equal(p.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(builder.count(p));
		criteriaQuery.where(predArray);
		TypedQuery<Long> typedQueryTotal = manager.createQuery(criteriaQuery);
		long totalAplicacaoes = typedQueryTotal.getSingleResult();
		
		return totalAplicacaoes;
	}

}
