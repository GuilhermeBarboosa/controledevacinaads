package web.controlevacinacao.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.support.SessionStatus;

import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.filter.AplicacaoFilter;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.model.filter.PessoaFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.AplicacaoRepository;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.service.AplicacaoService;
import web.controlevacinacao.service.LoteService;

@Controller
@RequestMapping("/aplicacoes")
public class AplicacaoController {

	private static final Logger logger = LoggerFactory.getLogger(AplicacaoController.class);
	
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LoteRepository loteRepository;
	
	@Autowired
	private AplicacaoService aplicacaoService;
	
	@Autowired
	private AplicacaoRepository aplicacaoRepository;
	
	@Autowired
	private LoteService loteService;
	
	@GetMapping("/cadastrar")
	public String abrirCadastro(HttpSession sessao) {
		Aplicacao aplicacao = buscarAplicacaoNaSessao(sessao);
		sessao.setAttribute("aplicacao", aplicacao);
		return "aplicacao/cadastrar";
	}

	private Aplicacao buscarAplicacaoNaSessao(HttpSession sessao) {
		Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");
		if (aplicacao == null) {
			aplicacao = new Aplicacao();
		}
		return aplicacao;
	}
	
	@GetMapping("/abrirescolherpessoa")
	public String abrirEscolhaPessoa() {
		return "aplicacao/escolherpessoa";
	}
	
	@GetMapping("/pesquisarpessoa")
	public String pesquisarPessoa(PessoaFilter filtro, Model model,
			@PageableDefault(size = 10) 
    		@SortDefault(sort = "codigo", direction = Sort.Direction.ASC)
    		Pageable pageable, HttpServletRequest request) {
		
		Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
		PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		
		return "aplicacao/mostrarpessoas";
	}
	
	@PostMapping("/escolherpessoa")
	public String escolherPessoa(Pessoa pessoa, HttpSession sessao) {
		Aplicacao aplicacao = buscarAplicacaoNaSessao(sessao);
		aplicacao.setPessoa(pessoa);
		sessao.setAttribute("aplicacao", aplicacao);
		return "aplicacao/cadastrar";
	}
		
	@GetMapping("/abrirescolherlote")
	public String abrirEscolhaLote() {
		return "aplicacao/escolherlote";
	}
	
	@GetMapping("/pesquisarlote")
	public String pesquisarLote(LoteFilter filtro, Model model,
			@PageableDefault(size = 10) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request) {
		Page<Lote> pagina = loteRepository.pesquisar(filtro, pageable, true);
		PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "aplicacao/mostrarlotes";
	}
	
	@PostMapping("/escolherlote")
	public String escolherLote(Lote lote, HttpSession sessao) {
		Aplicacao aplicacao = buscarAplicacaoNaSessao(sessao);
		aplicacao.setLote(lote);
		sessao.setAttribute("aplicacao", aplicacao);
		return "aplicacao/cadastrar";
	}
	
	@GetMapping("/efetuarcadastro")
	public String cadastrar(HttpSession sessao, SessionStatus status) {
		Aplicacao aplicacao = buscarAplicacaoNaSessao(sessao);
		aplicacao.setData(LocalDate.now());
		aplicacaoService.salvar(aplicacao);
		aplicacao.getLote().setNroDosesAtual(aplicacao.getLote().getNroDosesAtual() - 1);
		loteService.alterar(aplicacao.getLote());
		status.setComplete();
		sessao.invalidate();
		return "redirect:/aplicacoes/cadastro/sucesso";
	}
	
	@GetMapping("/cadastro/sucesso")
	public String mostrarMensagemCadastroSucesso(Model model) {
		model.addAttribute("mensagem", "Cadastro de Aplicação efetuado com sucesso.");
		return "mostrarmensagem";
	}
	
	@GetMapping("/abrirpesquisar")
	public String pesquisarAplicacao(Model model, HttpSession sessao) {
		Aplicacao aplicacao = buscarAplicacaoNaSessao(sessao);
		List<Lote> lotes = loteRepository.findByStatus(Status.ATIVO);
		List<Pessoa> pessoas = pessoaRepository.findByStatus(Status.ATIVO);
		model.addAttribute("pessoas", pessoas);
		model.addAttribute("lotes", lotes);
		return "aplicacao/pesquisar";
	}
	
	@GetMapping("/pesquisaraplicacao")
	public String pesquisarAplicacao(AplicacaoFilter aplicacao, Model model,
			@PageableDefault(size = 10) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request) {
		Page<Aplicacao> pagina = aplicacaoRepository.pesquisar(aplicacao, pageable);
		PageWrapper<Aplicacao> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "aplicacao/mostraraplicacao";
	}
	
}
