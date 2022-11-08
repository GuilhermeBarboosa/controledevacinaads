package web.controlevacinacao.ajax;

import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webmvc.SpringWebMvcThymeleafRequestContext;

@Component
public class ThymeleafUtil {

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private ServletContext servletContext; 

	public String processThymeleafTemplate(HttpServletRequest request,
										   HttpServletResponse response, 
										   Map<String, Object> atributos, 
										   String templateName) {
		return processThymeleafTemplate(request, response, atributos, templateName, null);
	}

	public String processThymeleafTemplate(HttpServletRequest request,
										   HttpServletResponse response, 
										   Map<String, Object> atributos,
										   String templateName,
										   String fragmentName) {
		WebContext context = new WebContext(request, response, servletContext);
		context.setVariables(atributos);
		RequestContext requestContext = new RequestContext(request, response, servletContext, atributos);
		SpringWebMvcThymeleafRequestContext thymeleafRequestContext = new SpringWebMvcThymeleafRequestContext(requestContext, request);
		context.setVariable("thymeleafRequestContext", thymeleafRequestContext);
		String html;
		if (fragmentName == null || fragmentName.isBlank()) {
			html = templateEngine.process(templateName, context);
		} else {
			html = templateEngine.process(templateName, Set.of(fragmentName), context);
		}
		return html;
	}

}