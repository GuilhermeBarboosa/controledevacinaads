package web.controlevacinacao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.repository.helper.aplicacao.AplicacaoQueries;

public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long>, AplicacaoQueries{

}
