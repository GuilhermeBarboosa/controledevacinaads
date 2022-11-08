package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.repository.AplicacaoRepository;

@Service
public class AplicacaoService {

	@Autowired
	private AplicacaoRepository aplicacaoRepository;
	
	@Transactional
	public void salvar(Aplicacao aplicacao) {
		aplicacaoRepository.save(aplicacao);
	}
	
}
