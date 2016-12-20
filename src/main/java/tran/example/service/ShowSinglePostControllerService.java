package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.BlogDAO;
import tran.example.presentation.model.Blog;

import java.security.Principal;

/**
 * @Author Todd
 * Handles the logic of showing a single post.
 */
@Service
public class ShowSinglePostControllerService {

    private static final String SHOW_SINGLE_POST_PAGE = "showSinglePost";

    private static final String ERROR_KEY = "error";

    private static final String LOGGED_IN_NAME_KEY = "loggedInName";

    private static final String IS_AUTHOR_OF_POST_KEY = "isAuthorOfPost";

    private static final String POST_CONTENTS_KEY = "postContents";

    private static final String INVALID_BLOG_ID_ERROR_MESSAGE = "you must provide a valid blog ID number.";

    private static final String BLOG_CANNOT_BE_FOUND_ERROR_MESSAGE = "A blog with that id could not be found.";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public String showSinglePost(Integer blogID, Principal principal, ModelMap model) {
        if(blogID == null) {
            model.addAttribute(ERROR_KEY, INVALID_BLOG_ID_ERROR_MESSAGE);
        }
        else {
            // open a connection to the database and get the necessary info
            ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
            BlogDAO getPost = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
            Blog singlePost = getPost.getaBlog(blogID);
            ((ConfigurableApplicationContext)appContext).close();
            if(singlePost != null) {
                // check if a user is logged in and if so then check if the user is the author of this post.
                if (principal != null) {
                    String userName = principal.getName();
                    if (userName != null) {
                        model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                        Boolean isAuthorOfPost = getPost.isAuthorOfPost(userName, blogID);
                        model.addAttribute(IS_AUTHOR_OF_POST_KEY, isAuthorOfPost);
                    }
                }
                if (singlePost.getAuthor() == null && singlePost.getContent() == null && singlePost.getTitle() == null)
                /* && singlePost.getDateCreated() == null && singlePost.getDateModified() == null*/ {
                    model.addAttribute(ERROR_KEY, BLOG_CANNOT_BE_FOUND_ERROR_MESSAGE);
                } else {
                    model.addAttribute(POST_CONTENTS_KEY, singlePost);
                }
            }
        }
        return SHOW_SINGLE_POST_PAGE;
    }
}
