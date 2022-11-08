package web.controlevacinacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.helper.profissao.ProfissaoQueries;

public interface ProfissaoRepository extends JpaRepository<Profissao, Long>, ProfissaoQueries {

	List<Profissao> findByStatus(Status status);
	
}
