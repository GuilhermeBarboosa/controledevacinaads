package web.controlevacinacao.repository.helper.pessoa;

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

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.filter.PessoaFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class PessoaQueriesImpl implements PessoaQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Pessoa> pesquisar(PessoaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		Root<Pessoa> p = criteriaQuery.from(Pessoa.class);
//		Join<Lote, Vacina> vacina = (Join)l.fetch("vacina", JoinType.INNER);
		p.fetch("profissao", JoinType.INNER);
		TypedQuery<Pessoa> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			predicateList.add(builder.like(	builder.lower(p.<String>get("cpf")),
										"%" + filtro.getCpf().toLowerCase() + "%"));
		}
		if (filtro.getDataNascimento() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					p.<LocalDate>get("dataNascimento"), 
		            filtro.getDataNascimento()));
		}
		if (filtro.getCodigoProfissao() != null) {
			predicateList.add(builder.equal(p.<Profissao>get("profissao").<Long>get("codigo"),
					filtro.getCodigoProfissao()));
		}
		
		predicateList.add(builder.equal(p.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(p).where(predArray);
		PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);
		typedQuery = manager.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
								
		List<Pessoa> pessoas = typedQuery.getResultList();
		
		long totalPessoas = total(filtro, pageable);
		
		Page<Pessoa> page = new PageImpl<>(pessoas, pageable, totalPessoas); 
		
		return page;
	}
	
	private long total(PessoaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Pessoa> p = criteriaQuery.from(Pessoa.class);
		Join<Pessoa, Profissao> profissao = p.join("profissao", JoinType.INNER);
		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), 
		                 filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			predicateList.add(builder.like(	builder.lower(p.<String>get("cpf")),
										"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (filtro.getDataNascimento() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(
					p.<LocalDate>get("dataNascimento"), 
		            filtro.getDataNascimento()));
		}
		if (filtro.getCodigoProfissao() != null) {
			predicateList.add(builder.equal(profissao.<Long>get("codigo"),
					filtro.getCodigoProfissao()));
		}
		
		predicateList.add(builder.equal(p.<Status>get("status"), 
		                 Status.ATIVO));
				
		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		criteriaQuery.select(builder.count(p));
		criteriaQuery.where(predArray);
		TypedQuery<Long> typedQueryTotal = manager.createQuery(criteriaQuery);
		long totalLotes = typedQueryTotal.getSingleResult();
		
		return totalLotes;
	}

}
