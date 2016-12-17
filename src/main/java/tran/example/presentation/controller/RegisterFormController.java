package tran.example.presentation.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tran.example.presentation.model.User;
import tran.example.data.UserDAO;

@Controller
public class RegisterFormController {

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String displayRegistration(ModelMap model) {
    	return "register";
	}
	
	@RequestMapping(value="/processRegistration", method=RequestMethod.POST)
	public String processRegistration(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "validatePassword", required = false) String validatePassword, ModelMap model) {
		if(userName != null && password != null && validatePassword != null) {
			User newUser = new User(userName, password, validatePassword);
			if(newUser.validate()) {
				// attempt to create the user.
				newUser.setUserRole("ROLE_USER");
				newUser.setEnabled(true);
				ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
				UserDAO createUser = (UserDAO)appContext.getBean("userDS");
				String createUserReturnCode = createUser.create(userName, newUser.encryptUserPassword(), newUser.getEnabled(), newUser.getUserRole());
				((ConfigurableApplicationContext)appContext).close();
				model.addAttribute("error", createUserReturnCode);
				return "register";
			}
			else { // failed validation, notify the user of the error
				String errorDescription = newUser.getMessage();
				if(!errorDescription.equals(""))
					model.addAttribute("error", errorDescription);
				return "register";
			}
		}
		else {
			// specific error where the fields could not be processed..
			model.addAttribute("error", "An error has occured, please try again");
			return "register";
		}
	}
	
	// url for the ajax request from the registration form.
	@ResponseBody
	@RequestMapping(value ="/userNameCheckDB", method=RequestMethod.GET, name = "yoyo")
	public ResponseEntity<String> clientSideCheck(@RequestParam(value = "userName", required = true) String userName) {
		ResponseEntity<String> returnCode = null;
		ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
		UserDAO createUser = (UserDAO)appContext.getBean("userDS");
		Integer createUserReturnCode = createUser.checkForUserName(userName);
		((ConfigurableApplicationContext)appContext).close();
		if(createUserReturnCode != null) {
			if(createUserReturnCode == 1)  { // user name does not exist
				// now check to see if it is in the proper format.
				User user = new User();
				returnCode = user.checkUserName(userName) == true ? new ResponseEntity<String>(HttpStatus.OK) 
						: new ResponseEntity<String>(user.getMessage(), HttpStatus.BAD_REQUEST); 
			}
			else if (createUserReturnCode == -1) {
				// user name exists or there was some kind of validation error.
				// a success code indicates that this username is taken.
				String error_message = "user name already exists";
				returnCode = new ResponseEntity<String>(error_message, HttpStatus.CONFLICT);
			}
			else if (createUserReturnCode == -2) {
				// SQL syntactical error, could not check if the user name exists.
				// a success code indicates that this username is taken.
				String server_error = "server error";
				returnCode = new ResponseEntity<String>(server_error, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return returnCode;
	}
	
}
