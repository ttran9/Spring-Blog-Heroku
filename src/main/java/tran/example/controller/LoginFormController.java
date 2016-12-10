package tran.example.controller;

import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tran.example.model.Blog;
import tran.example.model.DAO.BlogDAO;

@Controller
public class LoginFormController {
	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String displayLogin(Principal principal, ModelMap model, @RequestParam(value = "message", required = false) String redirectMessage) {
		if(principal != null) { // user is already logged in so just send him/her to the showPosts page.
			String userName = principal.getName();
			if(userName != null) {
				model.addAttribute("loggedInName", userName);
				model.addAttribute("error", "you are already logged in, there is no need for you to log in again.");
			}
			ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
			BlogDAO getPosts = (BlogDAO)appContext.getBean("BlogDS");
			List<Blog> listOfBlogs = getPosts.getBlogs();
			((ConfigurableApplicationContext)appContext).close();
			
			model.addAttribute("blogs", listOfBlogs);
	
			return "showPosts";
		}
		else {
			// not logged in
			if(redirectMessage != null) // if not logged in and redirected.
				model.addAttribute("error", redirectMessage);
			return "signin";
		}
	}
	
	@RequestMapping(value = "/signinError", method=RequestMethod.GET)
	public String displayLoginError(ModelMap model, HttpServletRequest request) {
		if(request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) {
			BadCredentialsException customError = (BadCredentialsException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			if(customError != null) {
				if(customError.getMessage() != null) {
					// this is a hack, not sure if it's a complete solution. may need to fix this down the road.
					String[] parsedErrorMessage = customError.getMessage().split(Pattern.quote(":"));
					if(parsedErrorMessage.length == 2) {
						model.addAttribute("userNameEntered", parsedErrorMessage[0]);
						model.addAttribute("error", parsedErrorMessage[1]);
					}
					else {
						model.addAttribute("error", customError.getMessage());
					}
				}
			}
			else {
				model.addAttribute("error", "an error has occured please try again");
			}
		}
		else {
			model.addAttribute("error", "invalid login credentials");
		}
		return "signin";
	}
	
}
