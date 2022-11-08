package web.controlevacinacao.repository.helper.profissao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.filter.ProfissaoFilter;

public interface ProfissaoQueries {

	Page<Profissao> pesquisar(ProfissaoFilter filtro, Pageable pageable);
}
