package web.controlevacinacao.repository.helper.lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.filter.LoteFilter;

public interface LoteQueries {

	Page<Lote> pesquisar(LoteFilter lote, Pageable pageable, boolean ehAplicacao);
	
}
