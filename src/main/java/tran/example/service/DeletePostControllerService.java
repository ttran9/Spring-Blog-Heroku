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
 * This class handles the logic to delete a post when a user is logged in.
 */
@Service
public class DeletePostControllerService {

    private static final String LOG_IN_PAGE = "signin";

    private static final String DELETE_POST_SUCCESS = "post successfully deleted!";

    private static final String NOT_AUTHOR_OF_POST = "You must be the author of the post to delete it.";

    private static final String NOT_LOGGED_IN = "You must be logged in before deleting a post.";

    private static final String ERROR_DELETING_POST_MESSAGE = "The post could not be deleted, make sure you are " +
            "passing in the proper blog ID.";

    private static final String ERROR_POST_DOES_NOT_EXIST = "The post does not exist so it cannot be deleted.";

    private static final String SHOW_POSTS_PAGE = "showPosts";

    private static final String SHOW_SINGLE_POST = "showSinglePost?blogID=";

    private static final String ERROR_KEY = "errorMessage";

    private static final String BLOGS_KEY = "blogs";

    private static final String USER_NAME_KEY = "loggedInName";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public boolean checkIfPostExists(String blogID, Principal principal) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                if(blogID != null && checkBlogIDValue(blogID)) {
                    BlogDAO checkForPost = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
                    Blog blog = checkForPost.getaBlog(Integer.parseInt(blogID));
                    if(blog != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String displayAllPosts(ModelMap model, Principal principal) {
        if(principal != null && principal.getName() != null)
            model.addAttribute(USER_NAME_KEY, principal.getName());
        ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
        model.addAttribute(ERROR_KEY, ERROR_POST_DOES_NOT_EXIST);
        return retrieveAllPosts(appContext, model);
    }

    public String processDeletePost(String blogID, Principal principal, ModelMap model) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                if(blogID != null && checkBlogIDValue(blogID)) {
                    return processDeletePostHelper(appContext, blogID, userName, model);
                }
                else {
                    return displayUnableToDeletePost(appContext, model);
                }
            }
            else {
                model.addAttribute(ERROR_KEY, NOT_LOGGED_IN);
                return LOG_IN_PAGE;
            }
        }
        else {
            model.addAttribute(ERROR_KEY, NOT_LOGGED_IN);
            return LOG_IN_PAGE;
        }
    }

    private String processDeletePostHelper(ApplicationContext appContext, String blogID, String userName,
                                           ModelMap model) {
        BlogDAO deletePost = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
        String deletePostMessage = deletePost.removePost(Integer.parseInt(blogID), userName);
        if(deletePostMessage.equals(DELETE_POST_SUCCESS)) {
            model.addAttribute(ERROR_KEY, deletePostMessage);
            model.addAttribute(USER_NAME_KEY, userName);
            BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
            List<Blog> listOfBlogs = getPosts.getBlogs();
            ((ConfigurableApplicationContext)appContext).close();
            if(listOfBlogs != null)
                model.addAttribute(BLOGS_KEY, listOfBlogs);
            return SHOW_POSTS_PAGE;
        }
        else {
            model.addAttribute(ERROR_KEY, NOT_AUTHOR_OF_POST);
            ((ConfigurableApplicationContext)appContext).close();
            return SHOW_SINGLE_POST + blogID;
        }
    }

    private String displayUnableToDeletePost(ApplicationContext appContext, ModelMap model) {
        model.addAttribute(ERROR_KEY, ERROR_DELETING_POST_MESSAGE);
        return retrieveAllPosts(appContext, model);
    }

    private String retrieveAllPosts(ApplicationContext appContext, ModelMap model) {
        BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
        List<Blog> listOfBlogs = getPosts.getBlogs();
        if(listOfBlogs != null)
            model.addAttribute(BLOGS_KEY, listOfBlogs);
        ((ConfigurableApplicationContext)appContext).close();
        return SHOW_POSTS_PAGE;
    }

    private boolean checkBlogIDValue(String BlogID) {
        int blogIDValue = Integer.parseInt(BlogID);
        return (blogIDValue > 0 && blogIDValue < Integer.MAX_VALUE);
    }
}
