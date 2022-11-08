package web.controlevacinacao.repository.helper.aplicacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.filter.AplicacaoFilter;
import web.controlevacinacao.model.filter.LoteFilter;

public interface AplicacaoQueries {

	Page<Aplicacao> pesquisar(AplicacaoFilter aplicacao, Pageable pageable);
	
}
