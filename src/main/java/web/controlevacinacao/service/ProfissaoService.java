package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.repository.ProfissaoRepository;

@Service
public class ProfissaoService {

	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Transactional
	public void salvar(Profissao profissao) {
		profissaoRepository.save(profissao);
	}
	
	@Transactional
	public void alterar(Profissao profissao) {
		profissaoRepository.save(profissao);
	}
	
	@Transactional
	public void remover(Long codigo) {
		profissaoRepository.deleteById(codigo);
	}
	
}
