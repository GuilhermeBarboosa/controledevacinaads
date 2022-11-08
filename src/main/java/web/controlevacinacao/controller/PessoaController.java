package web.controlevacinacao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.filter.PessoaFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.repository.ProfissaoRepository;
import web.controlevacinacao.service.PessoaService;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;

	@GetMapping("/abrirpesquisar")
	public String abrirPesquisa(Model model) {
		List<Profissao> profissoes = profissaoRepository.findByStatus(Status.ATIVO);
		model.addAttribute("profissoes", profissoes);
		return "pessoa/pesquisar";
	}
	
	@GetMapping("/pesquisar")
	public String pesquisar(PessoaFilter filtro, Model model,
			@PageableDefault(size = 10) 
    		@SortDefault(sort = "codigo", direction = Sort.Direction.ASC)
    		Pageable pageable, HttpServletRequest request) {
		
		Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
		PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "pessoa/mostrartodas";
	}
	
	@GetMapping("/cadastrar")
	public String abrirCadastro(Pessoa pessoa, Model model) {
		List<Profissao> profissoes = profissaoRepository.findByStatus(Status.ATIVO);
		model.addAttribute("profissoes", profissoes);
		return "pessoa/cadastrar";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(Pessoa pessoa) {
		pessoaService.salvar(pessoa);
		return "redirect:/pessoas/cadastro/sucesso";
	}
	
	@GetMapping("/cadastro/sucesso")
	public String mostrarMensagemCadastroSucesso(Model model) {
		model.addAttribute("mensagem", "Cadastro de Pessoa efetuado com sucesso.");
		return "mostrarmensagem";
	}
	
	@PostMapping("/abriralterar")
	public String abrirAlterar(Pessoa pessoa, Model model) {
		List<Profissao> profissoes = profissaoRepository.findByStatus(Status.ATIVO);
		model.addAttribute("profissoes", profissoes);
		return "pessoa/alterar";
	}
	
	@PostMapping("/alterar")
	public String alterar(Pessoa pessoa) {
		pessoaService.alterar(pessoa);
		return "redirect:/pessoas/alterar/sucesso";
	}
	
	@GetMapping("/alterar/sucesso")
	public String mostrarMensagemAlterarSucesso(Model model) {
		model.addAttribute("mensagem", "Alteração do Pessoa efetuada com sucesso.");
		return "mostrarmensagem";
	}
	
	@PostMapping("/abrirremover")
	public String abrirRemover(Pessoa pessoa) {
		return "pessoa/remover";
	}
	
	@PostMapping("/remover")
	public String remover(Pessoa pessoa) {
		pessoa.setStatus(Status.INATIVO);
		pessoaService.alterar(pessoa);
		return "redirect:/pessoas/remover/sucesso";
	}
	
	@GetMapping("/remover/sucesso")
	public String mostrarMensagemRemoverSucesso(Model model) {
		model.addAttribute("mensagem", "Remoção (INATIVO) de Pessoa efetuada com sucesso.");
		return "mostrarmensagem";
	}

}
