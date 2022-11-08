package web.controlevacinacao.controller;

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

import web.controlevacinacao.model.Profissao;
import web.controlevacinacao.model.filter.ProfissaoFilter;
import web.controlevacinacao.repository.ProfissaoRepository;
import web.controlevacinacao.service.ProfissaoService;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.pagination.PageWrapper;

@Controller
@RequestMapping("/profissoes")
public class ProfissaoController {

	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Autowired
	private ProfissaoService profissaoService;

	@GetMapping("/abrirpesquisar")
	public String abrirPesquisa() {
		return "profissao/pesquisar";
	}
	
	@GetMapping("/pesquisar")
	public String pesquisar(ProfissaoFilter filtro, Model model, 
			@PageableDefault(size = 10) 
    		@SortDefault(sort = "codigo", direction = Sort.Direction.ASC)
    		Pageable pageable, HttpServletRequest request) {
		
		Page<Profissao> pagina = profissaoRepository.pesquisar(filtro, pageable);
		PageWrapper<Profissao> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "profissao/mostrartodas";
	}
	
	@GetMapping("/cadastrar")
	public String abrirCadastro(Profissao profissao) {
		return "profissao/cadastrar";
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(Profissao profissao) {
		profissaoService.salvar(profissao);
		return "redirect:/profissoes/cadastro/sucesso";
	}
	
	@GetMapping("/cadastro/sucesso")
	public String mostrarMensagemCadastroSucesso(Model model) {
		model.addAttribute("mensagem", "Cadastro de Profissão efetuado com sucesso.");
		return "mostrarmensagem";
	}
	
	@PostMapping("/abriralterar")
	public String abrirAlterar(Profissao profissao) {
		return "profissao/alterar";
	}
	
	@PostMapping("/alterar")
	public String alterar(Profissao profissao) {
		profissaoService.alterar(profissao);
		return "redirect:/profissoes/alterar/sucesso";
	}
	
	@GetMapping("/alterar/sucesso")
	public String mostrarMensagemAlterarSucesso(Model model) {
		model.addAttribute("mensagem", "Alteração na Profissão efetuada com sucesso.");
		return "mostrarmensagem";
	}
	
	@PostMapping("/abrirremover")
	public String abrirRemover(Profissao profissao) {
		return "profissao/remover";
	}
	
	@PostMapping("/remover")
	public String remover(Profissao profissao) {
		profissao.setStatus(Status.INATIVO);
		profissaoService.alterar(profissao);
		return "redirect:/profissoes/remover/sucesso";
	}
	
	@GetMapping("/remover/sucesso")
	public String mostrarMensagemRemoverSucesso(Model model) {
		model.addAttribute("mensagem", "Remoção (INATIVO) de Profissão efetuada com sucesso.");
		return "mostrarmensagem";
	}

}
