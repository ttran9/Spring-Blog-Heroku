package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tran.example.service.RegisterFormControllerService;

@Controller
public class RegisterFormController {

	private static final String DISPLAY_REGISTRATION_MAPPING = "/register";

	private static final String PROCESS_REGISTRATION_MAPPING = "/processRegistration";

	private static final String USER_NAME_CHECK_AJAX_MAPPING = "/userNameCheckDB";

	private static final String USER_NAME_PARAM = "userName";

	private static final String PASSWORD_PARAM = "password";

	private static final String VALIDATE_PASSWORD_PARAM = "validatePassword";

	@Autowired
    RegisterFormControllerService registerFormControllerService;

	@RequestMapping(value=DISPLAY_REGISTRATION_MAPPING, method=RequestMethod.GET)
	public String displayRegistration(ModelMap model) {
    	return DISPLAY_REGISTRATION_MAPPING;
	}
	
	@RequestMapping(value=PROCESS_REGISTRATION_MAPPING, method=RequestMethod.POST)
	public String processRegistration(@RequestParam(value=USER_NAME_PARAM, required=false) String userName,
									  @RequestParam(value=PASSWORD_PARAM, required=false) String password,
									  @RequestParam(value=VALIDATE_PASSWORD_PARAM, required=false)
												  String validatePassword, ModelMap model) {
		return registerFormControllerService.processRegistration(userName, password, validatePassword, model);
	}
	
	// url for the ajax request from the registration form.
	@ResponseBody
	@RequestMapping(value=USER_NAME_CHECK_AJAX_MAPPING, method=RequestMethod.GET)
	public ResponseEntity<String> clientSideCheck(@RequestParam(value=USER_NAME_PARAM, required=true) String userName) {
		return registerFormControllerService.clientSideCheck(userName);
	}
	
}
