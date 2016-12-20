package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.BlogDAO;
import tran.example.presentation.model.Blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

/**
 * @Author
 * Handles the logic of logging a logged in user out and one that isn't logged in.
 */
@Service
public class LogoutControllerService {

    private static final String SHOW_POSTS_PAGE = "showPosts";

    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    private static final String BLOGS_KEY = "blogs";

    private static final String LOGGED_OUT_MESSAGE = "logged out!";

    private static final String NOT_LOGGED_IN_MESSAGE = "you were never logged in.";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public String showLogoutPage(HttpServletRequest request, HttpServletResponse response, Principal principal,
                                 ModelMap model) {
        if (principal != null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return showLogoutPageHelper(model, LOGGED_OUT_MESSAGE);
        }
        else {
            return showLogoutPageHelper(model, NOT_LOGGED_IN_MESSAGE);
        }
    }

    private String showLogoutPageHelper(ModelMap model, String errorMessage) {
        ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
        BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
        List<Blog> listOfBlogs = getPosts.getBlogs();
        ((ConfigurableApplicationContext)appContext).close();
        model.addAttribute(ERROR_MESSAGE_KEY, errorMessage);
        if(listOfBlogs != null)
            model.addAttribute(BLOGS_KEY, listOfBlogs);
        return SHOW_POSTS_PAGE;
    }
}
