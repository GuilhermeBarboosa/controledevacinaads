package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.controlevacinacao.service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {
	
private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);
	
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping("/todasaplicacoes")
	public ResponseEntity<byte[]> gerarRelatorioSimplesTodasAplicacoes() {
		logger.trace("Entrou em gerarRelatorioSimplesAplicacoes");
		logger.debug("Gerando relatório simples de todas as aplicacoes");
		
		byte[] relatorio = relatorioService.gerarRelatorioSimplesTodasAplicacoes();
		
		logger.debug("Relatório simples de todas as aplicacoes gerado");
		logger.debug("Retornando o relatório simples de todas as aplicacoes");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AplicacoesSimples.pdf")
				.body(relatorio);
	}

}