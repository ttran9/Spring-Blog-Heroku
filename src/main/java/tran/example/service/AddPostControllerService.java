package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tran.example.data.BlogDAO;
import tran.example.data.UserDAO;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @Author Todd
 * This class handles the logic for when a user attempts to add a post and to display the add a post page.
 */
@Service
public class AddPostControllerService {

    private static final String ADD_POST_PAGE = "addPost";

    private static final String SIGNIN_PAGE = "redirect:/displayAddForm";

    private static final String LOGGED_IN_NAME_KEY = "loggedInName";

    private static final String ENTERED_TITLE_KEY = "entered_title";

    private static final String MESSAGE_KEY = "message";

    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    private static final String ENTERED_CONTENT_KEY = "entered_content";

    private static final String NOT_LOGGED_IN_ERROR_MESSAGE = "You must be logged in before viewing this page.";

    private static final String NOT_LOGGED_IN_BEFORE_CREATING_POST = "You must be logged in before creating a post.";

    /** An error message to indicate the user has not waited 30 seconds before adding or editing a post */
    private static final String POST_ADD_OR_MODIFY_QUICKLY = "You must wait 30 seconds from editing or posting to " +
            "add another post.";

    private static final String USER_DAO_BEAN_NAME = "userDS";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    private static final String REDIRECT_TO_SINGLE_POST = "redirect:showSinglePost?blogID=";

    public String displayAddForm(Principal principal, ModelMap model, RedirectAttributes requestAttributes) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                return ADD_POST_PAGE;
            }
            else {
                requestAttributes.addAttribute(MESSAGE_KEY, NOT_LOGGED_IN_ERROR_MESSAGE);
                return SIGNIN_PAGE;
            }
        }
        else {
            requestAttributes.addAttribute(MESSAGE_KEY, NOT_LOGGED_IN_ERROR_MESSAGE);
            return SIGNIN_PAGE;
        }
    }

    public String processAddForm(String title, String content, Principal principal, ModelMap model,
                                 RedirectAttributes requestAttributes) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                UserDAO userDAO = (UserDAO)appContext.getBean(USER_DAO_BEAN_NAME);
                if(userDAO.canUserPost(userName)) {
                    BlogDAO getPosts = (BlogDAO) appContext.getBean(BLOG_DAO_BEAN_NAME);
                    ((ConfigurableApplicationContext) appContext).close();
                    int createPostMessage = getPosts.addBlog(title, content, userName);
                    if(createPostMessage != -1) {
                        LocalDateTime current_time = Timestamp.from(Instant.now()).toLocalDateTime();
                        userDAO.updateLastPostedTimeForUser(current_time, userName);
                    }
                    model.addAttribute(ERROR_MESSAGE_KEY, "post has been added!");
                    return REDIRECT_TO_SINGLE_POST + createPostMessage;
                }
                else {
                    model.addAttribute(ENTERED_TITLE_KEY, title);
                    model.addAttribute(ERROR_MESSAGE_KEY, POST_ADD_OR_MODIFY_QUICKLY);
                    model.addAttribute(ENTERED_CONTENT_KEY, content);
                    model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                    return ADD_POST_PAGE;
                }
            }
            else {
                requestAttributes.addAttribute(MESSAGE_KEY, NOT_LOGGED_IN_BEFORE_CREATING_POST);
                return SIGNIN_PAGE;
            }
        }
        else {
            requestAttributes.addAttribute(MESSAGE_KEY, NOT_LOGGED_IN_BEFORE_CREATING_POST);
            return SIGNIN_PAGE;
        }
    }

}
