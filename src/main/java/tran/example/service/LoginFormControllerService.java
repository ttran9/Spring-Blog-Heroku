package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.BlogDAO;
import tran.example.presentation.model.Blog;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author Todd
 * Handles the logic when a user attempts to log in and displays the login page to the user.
 */
@Service
public class LoginFormControllerService {

    private static final String SHOW_POSTS_PAGE = "showPosts";

    private static final String LOGGED_IN_NAME_KEY = "loggedInName";

    private static final String SIGNIN_PAGE = "signin";

    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    private static final String MESSAGE_KEY = "error";

    private static final String BLOGS_KEY = "blogs";

    private static final String USER_NAME_ENTERED_KEY = "userNameEntered";

    private static final String LOGGED_IN_ERROR_MESSAGE = "you are already logged in, there is no need for you " +
            "to log in again.";

    private static final String GENERIC_LOGIN_ERROR_MESSAGE = "an error has occurred please try again";

    private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "invalid login credentials";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    private static final String SPRING_SECURITY_LAST_EXCEPTION_STRING = "SPRING_SECURITY_LAST_EXCEPTION";

    public String displayLogin(Principal principal, ModelMap model, String redirectMessage) {
        if(principal != null) { // user is already logged in so just send him/her to the showPosts page.
            String userName = principal.getName();
            if(userName != null) {
                model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                model.addAttribute(ERROR_MESSAGE_KEY, LOGGED_IN_ERROR_MESSAGE);
            }
            ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
            BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
            List<Blog> listOfBlogs = getPosts.getBlogs();
            ((ConfigurableApplicationContext)appContext).close();

            if(listOfBlogs != null)
                model.addAttribute(BLOGS_KEY, listOfBlogs);

            return SHOW_POSTS_PAGE;
        }
        else {
            // not logged in
            if(redirectMessage != null) // if not logged in and redirected.
                model.addAttribute(MESSAGE_KEY, redirectMessage);
            return SIGNIN_PAGE;
        }
    }

    public String displayLoginError(ModelMap model, HttpServletRequest request) {
        if(request.getSession().getAttribute(SPRING_SECURITY_LAST_EXCEPTION_STRING) != null) {
            BadCredentialsException customError = (BadCredentialsException) request.getSession().
                    getAttribute(SPRING_SECURITY_LAST_EXCEPTION_STRING);
            if(customError != null) {
                if(customError.getMessage() != null) {
                    // this is a hack, not sure if it's a complete solution. may need to fix this down the road.
                    String[] parsedErrorMessage = customError.getMessage().split(Pattern.quote(":"));
                    if(parsedErrorMessage.length == 2) {
                        model.addAttribute(USER_NAME_ENTERED_KEY, parsedErrorMessage[0]);
                        model.addAttribute(MESSAGE_KEY, parsedErrorMessage[1]);
                    }
                    else {
                        model.addAttribute(MESSAGE_KEY, customError.getMessage());
                    }
                }
            }
            else {
                model.addAttribute(MESSAGE_KEY, GENERIC_LOGIN_ERROR_MESSAGE);
            }
        }
        else {
            model.addAttribute(MESSAGE_KEY, INVALID_CREDENTIALS_ERROR_MESSAGE);
        }
        return SIGNIN_PAGE;
    }
}
