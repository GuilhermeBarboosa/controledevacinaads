package web.controlevacinacao.controller;

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

import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.VacinaFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.VacinaService;

@Controller
@RequestMapping("/vacinas")
public class VacinaController {

	private static final Logger logger = LoggerFactory.getLogger(VacinaController.class);

	@Autowired
	private VacinaRepository vacinaRepository;

	@Autowired
	private VacinaService vacinaService;

	@GetMapping("/abrirpesquisar")
	public String abrirPesquisa() {
		return "vacina/pesquisar";
	}

	@GetMapping("/pesquisar")
	public String pesquisar(VacinaFilter filtro, Model model,
			@PageableDefault(size = 10) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request) {

		Page<Vacina> pagina = vacinaRepository.pesquisar(filtro, pageable);
		PageWrapper<Vacina> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "vacina/mostrartodas";
	}

	@GetMapping("/cadastrar")
	public String abrirCadastro(Vacina vacina) {
		return "vacina/cadastrar";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@Valid Vacina vacina, BindingResult resultado) {
		if (resultado.hasErrors()) {
			logger.info("A vacina recabida para cadastrar não é válida.");
			logger.info("Erros encontrados:");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			return "vacina/cadastrar";
		} else {
			vacinaService.salvar(vacina);
			return "redirect:/vacinas/cadastro/sucesso";
		}
	}

	@GetMapping("/cadastro/sucesso")
	public String mostrarMensagemCadastroSucesso(Vacina vacina, Model model) {
		NotificacaoAlertify notificacao = new NotificacaoAlertify("Cadastro de Vacina efetuado com sucesso.", TipoNotificaoAlertify.SUCESSO);
		model.addAttribute("notificacao", notificacao);
		return "vacina/cadastrar";
	}
	
//	@GetMapping("/cadastro/sucesso")
//	public String mostrarMensagemCadastroSucesso(Model model) {
//		model.addAttribute("mensagem", "Cadastro de Vacina efetuado com sucesso.");
//		return "mostrarmensagem";
//	}

	@PostMapping("/abriralterar")
	public String abrirAlterar(Vacina vacina) {
		return "vacina/alterar";
	}

	@PostMapping("/alterar")
	public String alterar(@Valid Vacina vacina, BindingResult resultado) {
		if (resultado.hasErrors()) {
			logger.info("A vacina recabida para alterar não é válida.");
			logger.info("Erros encontrados:");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			return "vacina/alterar";
		} else {
			vacinaService.alterar(vacina);
			return "redirect:/vacinas/alterar/sucesso";
		}
	}

	@GetMapping("/alterar/sucesso")
	public String mostrarMensagemAlterarSucesso(Model model) {
		NotificacaoAlertify notificacao = new NotificacaoAlertify("Alteração efetuada com sucesso.", TipoNotificaoAlertify.SUCESSO);
		model.addAttribute("notificacao", notificacao);
		return "vacina/pesquisar";
	}

	@PostMapping("/abrirremover")
	public String abrirRemover(Vacina vacina) {
		return "vacina/remover";
	}

	@PostMapping("/remover")
	public String remover(Vacina vacina) {
		vacina.setStatus(Status.INATIVO);
		vacinaService.alterar(vacina);
		return "redirect:/vacinas/remover/sucesso";
	}

	@GetMapping("/remover/sucesso")
	public String mostrarMensagemRemoverSucesso(Model model) {
		NotificacaoAlertify notificacao = new NotificacaoAlertify("Remoção (INATIVO) efetuada com sucesso.", TipoNotificaoAlertify.SUCESSO);
		model.addAttribute("notificacao", notificacao);
		return "vacina/pesquisar";
	}

}
