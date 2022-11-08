package web.controlevacinacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.helper.lote.LoteQueries;

public interface LoteRepository extends JpaRepository<Lote, Long>, LoteQueries {

	List<Lote> findByStatus(Status ativo);

}
