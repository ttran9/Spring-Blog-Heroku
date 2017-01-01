package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.service.LoginFormControllerService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class LoginFormController {

	private static final String DISPLAY_LOG_IN_MAPPING = "/signin";

	private static final String DISPLAY_LOG_IN_ERROR_MAPPING = "/signinError";

	private static final String REDIRECT_MESSAGE_PARAM = "message";

	@Autowired
    LoginFormControllerService loginFormControllerService;

	@RequestMapping(value=DISPLAY_LOG_IN_MAPPING, method=RequestMethod.GET)
	public String displayLogin(Principal principal, ModelMap model, @RequestParam(value=REDIRECT_MESSAGE_PARAM, required=false) String message) {
		return loginFormControllerService.displayLogin(principal, model, message);
	}
	
	@RequestMapping(value=DISPLAY_LOG_IN_ERROR_MAPPING, method=RequestMethod.GET)
	public String displayLoginError(ModelMap model, HttpServletRequest request) {
		return loginFormControllerService.displayLoginError(model, request);
	}
}
