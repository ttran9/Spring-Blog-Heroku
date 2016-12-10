package tran.example.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogoutController {
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response, ModelMap model, Principal principal, RedirectAttributes requestattributes) {
	    if (principal != null){    
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	        requestattributes.addAttribute("message", "logged out!");
	        return "redirect:/signin";
	    }
	    else {
	    	model.addAttribute("error", "you were never logged in.");
	    	return "signin";
	    }
	}
}
