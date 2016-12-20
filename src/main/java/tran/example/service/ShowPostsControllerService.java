package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.BlogDAO;
import tran.example.presentation.model.Blog;

import java.security.Principal;
import java.util.List;

/**
 * @Author Todd
 * Handles the logic to display all the posts in the database.
 */
@Service
public class ShowPostsControllerService {

    private static final String SHOW_POSTS_PAGE = "showPosts";

    private static final String LOGGED_IN_NAME_KEY = "loggedInName";

    private static final String BLOGS_KEY = "blogs";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public String showPosts(Principal principal, ModelMap model) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                model.addAttribute(LOGGED_IN_NAME_KEY, userName);
            }
        }
        ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
        BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
        List<Blog> listOfBlogs = getPosts.getBlogs();
        ((ConfigurableApplicationContext)appContext).close();
        if(listOfBlogs != null)
            model.addAttribute(BLOGS_KEY, listOfBlogs);

        return SHOW_POSTS_PAGE;
    }
}
