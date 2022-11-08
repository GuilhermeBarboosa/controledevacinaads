package web.controlevacinacao.repository.helper.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.filter.PessoaFilter;

public interface PessoaQueries {

	Page<Pessoa> pesquisar(PessoaFilter lote, Pageable pageable);
	
}
