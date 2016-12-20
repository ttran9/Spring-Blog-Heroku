package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.UserDAO;
import tran.example.presentation.model.User;

/**
 * @Author Todd
 * Handles when a user attempts to register and does the registration processing.
 */
@Service
public class RegisterFormControllerService {

    private static final String USER_ROLE = "ROLE_USER";

    private static final String SIGN_IN_PAGE = "signin";

    private static final String REGISTER_PAGE = "register";

    private static final String ERROR_KEY = "error";

    private static final String GENERIC_ERROR_MESSAGE = "An error has occured, please try again";

    private static final String USER_NAME_EXISTS_ERROR = "user name already exists";

    private static final String SERVER_ERROR_MESSAGE = "server error";

    private static final String USER_DAO_BEAN_NAME = "userDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public String processRegistration(String userName, String password, String validatePassword, ModelMap model) {
        if(userName != null && password != null && validatePassword != null) {
            User newUser = new User(userName, password, validatePassword);
            UserService userService = new UserService(userName, password, validatePassword);
            if(userService.validate()) {
                // attempt to create the user.
                newUser.setUserRole(USER_ROLE);
                newUser.setEnabled(true);
                ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                UserDAO createUser = (UserDAO)appContext.getBean(USER_DAO_BEAN_NAME);
                String createUserReturnCode = createUser.create(userName, newUser.encryptUserPassword(), newUser.getEnabled(), newUser.getUserRole());
                ((ConfigurableApplicationContext)appContext).close();
                model.addAttribute(ERROR_KEY, createUserReturnCode);
                return SIGN_IN_PAGE;
            }
            else { // failed validation, notify the user of the error
                String errorDescription = newUser.getMessage();
                if(!errorDescription.equals(""))
                    model.addAttribute(ERROR_KEY, errorDescription);
                return REGISTER_PAGE;
            }
        }
        else {
            // specific error where the fields could not be processed..
            model.addAttribute(ERROR_KEY, GENERIC_ERROR_MESSAGE);
            return REGISTER_PAGE;
        }
    }

    // url for the ajax request from the registration form.
    public ResponseEntity<String> clientSideCheck(String userName) {
        ResponseEntity<String> returnCode = null;
        ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
        UserDAO createUser = (UserDAO)appContext.getBean(USER_DAO_BEAN_NAME);
        Integer createUserReturnCode = createUser.checkForUserName(userName);
        ((ConfigurableApplicationContext)appContext).close();
        if(createUserReturnCode == 1)  { // user name does not exist
            // now check to see if it is in the proper format.
            UserService userService = new UserService();
            returnCode = userService.checkUserName(userName) ? new ResponseEntity<String>(HttpStatus.OK)
                    : new ResponseEntity<String>(userService.getMessage(), HttpStatus.BAD_REQUEST);
        }
        else if (createUserReturnCode == -1) {
            // user name exists or there was some kind of validation error.
            // a success code indicates that this username is taken.
            returnCode = new ResponseEntity<String>(USER_NAME_EXISTS_ERROR, HttpStatus.CONFLICT);
        }
        else if (createUserReturnCode == -2) {
            // SQL syntactical error, could not check if the user name exists.
            // a success code indicates that this username is taken.
            returnCode = new ResponseEntity<String>(SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return returnCode;
    }
}
