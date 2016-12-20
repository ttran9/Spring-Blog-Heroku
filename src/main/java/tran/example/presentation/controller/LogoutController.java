package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tran.example.service.LogoutControllerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class LogoutController {

	private static final String SHOW_LOGOUT_PAGE_MAPPING = "/logout";

	@Autowired
    LogoutControllerService logoutControllerService;

	@RequestMapping(value=SHOW_LOGOUT_PAGE_MAPPING, method = RequestMethod.GET)
	public String showLogoutPage(HttpServletRequest request, HttpServletResponse response, Principal principal,
								 ModelMap model) {
	    return logoutControllerService.showLogoutPage(request, response, principal, model);
	}
}
