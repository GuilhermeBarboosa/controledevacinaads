package web.controlevacinacao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.LoteService;

@Controller
@RequestMapping("/lotes")
public class LoteController {

	private static final Logger logger = LoggerFactory.getLogger(LoteController.class);

	@Autowired
	private VacinaRepository vacinaRepository;

	@Autowired
	private LoteRepository loteRepository;

	@Autowired
	private LoteService loteService;

	@GetMapping("/abrirpesquisar")
	public String abrirPesquisa(Model model) {
		List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
		model.addAttribute("vacinas", vacinas);
		return "lote/pesquisar";
	}

	@GetMapping("/pesquisar")
	public String pesquisar(LoteFilter filtro, Model model,
			@PageableDefault(size = 10) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request) {
		Page<Lote> pagina = loteRepository.pesquisar(filtro, pageable, false);
		PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "lote/mostrartodos";
	}

	@GetMapping("/cadastrar")
	public String abrirCadastro(Lote lote, Model model) {
		List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
		model.addAttribute("vacinas", vacinas);
		return "lote/cadastrar";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@Valid Lote lote, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			logger.info("O lote recebido para cadastrar não é válido.");
			logger.info("Erros encontrados:");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
			model.addAttribute("vacinas", vacinas);
			return "lote/cadastrar";
		} else {
			loteService.salvar(lote);
			return "redirect:/lotes/cadastro/sucesso";
		}
	}

	@GetMapping("/cadastro/sucesso")
	public String mostrarMensagemCadastroSucesso(Model model) {
		model.addAttribute("mensagem", "Cadastro de lote efetuado com sucesso.");
		return "mostrarmensagem";
	}

	@PostMapping("/abriralterar")
	public String abrirAlterar(Lote lote, Model model) {
		List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
		model.addAttribute("vacinas", vacinas);
		return "lote/alterar";
	}

	@PostMapping("/alterar")
	public String alterar(@Valid Lote lote, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			logger.info("O lote recebido para alterar não é válido.");
			logger.info("Erros encontrados:");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
			model.addAttribute("vacinas", vacinas);
			return "lote/alterar";
		} else {
			loteService.alterar(lote);
			return "redirect:/lotes/alterar/sucesso";
		}
	}

	@GetMapping("/alterar/sucesso")
	public String mostrarMensagemAlterarSucesso(Model model) {
		model.addAttribute("mensagem", "Alteração do lote efetuada com sucesso.");
		return "mostrarmensagem";
	}

	@PostMapping("/abrirremover")
	public String abrirRemover(Lote lote) {
		return "lote/remover";
	}

	@PostMapping("/remover")
	public String remover(Lote lote) {
		lote.setStatus(Status.INATIVO);
		loteService.alterar(lote);
		return "redirect:/lotes/remover/sucesso";
	}

	@GetMapping("/remover/sucesso")
	public String mostrarMensagemRemoverSucesso(Model model) {
		model.addAttribute("mensagem", "Remoção (INATIVO) de Lote efetuada com sucesso.");
		return "mostrarmensagem";
	}

}
