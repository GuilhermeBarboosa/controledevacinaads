package web.controlevacinacao.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import web.controlevacinacao.model.Aplicacao;

@Component
@Order(1)
public class AplicacaoSessionFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(AplicacaoSessionFilter.class);

	@Override
	public void doFilter(ServletRequest request,
			             ServletResponse response, 
			             FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession sessao = req.getSession(false); // false para nao criar uma caso nao exista
		if (sessao != null) {
			String url = req.getRequestURI();
			logger.info("Filtrando a URL: {}", url);
			if (!url.startsWith("/aplicacoes") && !url.startsWith("/css") 
				&& !url.startsWith("/js") && !url.startsWith("/images")) {
				Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");
				if (aplicacao != null) {
					aplicacao.setLote(null);
					aplicacao.setPessoa(null);
					sessao.setAttribute("aplicacao", null);
					logger.info("Limpou a aplicacao da sessao atual.");
				}
			}
		}
		chain.doFilter(request, response);  // deixa o atendimento normal da requisicao continuar.
	}

}
