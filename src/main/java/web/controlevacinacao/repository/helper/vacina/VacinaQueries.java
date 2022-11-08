package web.controlevacinacao.repository.helper.vacina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.VacinaFilter;

public interface VacinaQueries {

	Page<Vacina> pesquisar(VacinaFilter filtro, Pageable pageable);
}
