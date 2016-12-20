package tran.example.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tran.example.data.BlogDAO;
import tran.example.data.UserDAO;
import tran.example.presentation.model.Blog;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Todd
 * Handles the logic to edit a post.
 */
@Service
public class EditPostControllerService {

    private static final String EDIT_POST_PAGE = "editPost";

    private static final String SHOW_POSTS_PAGE = "showPosts";

    private static final String SIGN_IN_PAGE = "signin";

    private static final String SHOW_SINGLE_POST = "showSinglePost";

    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    private static final String LOGGED_IN_NAME_KEY = "loggedInName";

    private static final String POST_TO_EDIT_KEY = "postToEdit";

    private static final String LIST_OF_BLOGS_KEY = "blogs";

    private static final String BLOG_CONTENT_KEY = "postContents";

    private static final String IS_AUTHOR_POST_KEY = "isAuthorOfPost";

    private static final String USER_DAO_BEAN_NAME = "userDS";

    private static final String BLOG_DAO_BEAN_NAME = "BlogDS";

    private static final String NOT_AUTHOR_OF_POST_ERROR = "You must be the author of a post to edit it.";

    private static final String INVALID_POST_ID_VALUE_ERROR = "The post could not be edited, make sure you are " +
            "passing in the proper blog ID.";

    private static final String NOT_LOGGED_IN_ERROR = "You must be logged in before editing a post.";

    private static final String NOT_ABLE_TO_RETRIEVE_POST_ERROR = "The post cannot be found.";

    private static final String POST_ADD_OR_MODIFY_QUICKLY = "You must wait at least 30 seconds from your last edit " +
            "or last posted time to edit a post.";

    private static final String PATH_TO_DATASOURCE_FILE = "spring/database/Datasource.xml";

    public String showEditPost(String blogID, Principal principal, ModelMap model) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                ApplicationContext appContext =  new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                if(blogID != null && checkBlogIDValue(blogID)) {
                    BlogDAO getPost = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
                    Blog thePost = getPost.getaBlog(Integer.parseInt(blogID));
                    ((ConfigurableApplicationContext)appContext).close();
                    if(!(thePost.getContent().equals("")) && getPost.isAuthorOfPost(userName, Integer.parseInt(blogID)))
                    {
                        model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                        model.addAttribute(POST_TO_EDIT_KEY, thePost);
                        return EDIT_POST_PAGE;
                    }
                    else {
                        model.addAttribute(ERROR_MESSAGE_KEY, NOT_AUTHOR_OF_POST_ERROR);
                        goToAllPostsPage(appContext, model);
                        return SHOW_POSTS_PAGE;
                    }
                }
                else {
                    model.addAttribute(ERROR_MESSAGE_KEY, INVALID_POST_ID_VALUE_ERROR);
                    goToAllPostsPage(appContext, model);
                    return SHOW_POSTS_PAGE;
                }
            }
        }
        model.addAttribute(ERROR_MESSAGE_KEY, NOT_LOGGED_IN_ERROR);
        return SIGN_IN_PAGE;
    }

    private void goToAllPostsPage(ApplicationContext appContext, ModelMap model) {
        BlogDAO getPosts = (BlogDAO)appContext.getBean(BLOG_DAO_BEAN_NAME);
        List<Blog> listOfBlogs = getPosts.getBlogs();
        ((ConfigurableApplicationContext)appContext).close();
        if(listOfBlogs != null)
            model.addAttribute(LIST_OF_BLOGS_KEY, listOfBlogs);
    }

    public String processEditPost(String blogID, String content, Principal principal, ModelMap model) {
        if(principal != null) {
            String userName = principal.getName();
            if(userName != null) {
                ApplicationContext appContext = new ClassPathXmlApplicationContext(PATH_TO_DATASOURCE_FILE);
                UserDAO userDAO = (UserDAO)appContext.getBean(USER_DAO_BEAN_NAME);
                if(userDAO.canUserPost(userName)) {
                   return editPostHelper(blogID, model, userName, appContext, userDAO, content);
                }
                else { // cannot edit because edited before 30 seconds.
                    return processCannotPost(blogID, model, userName, appContext, content);
                }
            }
            else {
                model.addAttribute(ERROR_MESSAGE_KEY, NOT_LOGGED_IN_ERROR + "\n" + NOT_AUTHOR_OF_POST_ERROR);
                return SIGN_IN_PAGE;
            }
        }
        else {
            model.addAttribute(ERROR_MESSAGE_KEY, NOT_LOGGED_IN_ERROR);
            return SIGN_IN_PAGE;
        }
    }

    private String editPostHelper(String blogID, ModelMap model, String userName, ApplicationContext appContext,
                                  UserDAO userDAO, String content) {
        if (blogID != null && checkBlogIDValue(blogID)) {
            BlogDAO editPost = (BlogDAO) appContext.getBean(BLOG_DAO_BEAN_NAME);
            // edit the post.
            String updatePostMessage = editPost.updatePost(Integer.parseInt(blogID), content);
            Blog newPost = editPost.getaBlog(Integer.parseInt(blogID));
            ((ConfigurableApplicationContext) appContext).close();
            if (newPost != null) {
                model.addAttribute(ERROR_MESSAGE_KEY, updatePostMessage);
                model.addAttribute(LOGGED_IN_NAME_KEY, userName);
                model.addAttribute(BLOG_CONTENT_KEY, newPost);
                boolean isAuthorOfPost = editPost.isAuthorOfPost(userName, Integer.parseInt(blogID));
                model.addAttribute(IS_AUTHOR_POST_KEY, isAuthorOfPost);
                LocalDateTime current_time = Timestamp.from(Instant.now()).toLocalDateTime();
                userDAO.updateLastPostedTimeForUser(current_time, userName);
                return SHOW_SINGLE_POST;
            } else {
                // since the post cannot be found do not pass in a blogID parameter.
                model.addAttribute(ERROR_MESSAGE_KEY, NOT_ABLE_TO_RETRIEVE_POST_ERROR);
                return SHOW_POSTS_PAGE;
            }
        } else {
            model.addAttribute(ERROR_MESSAGE_KEY, INVALID_POST_ID_VALUE_ERROR);
            BlogDAO getPosts = (BlogDAO) appContext.getBean(BLOG_DAO_BEAN_NAME);
            List<Blog> listOfBlogs = getPosts.getBlogs();
            ((ConfigurableApplicationContext) appContext).close();
            if (listOfBlogs != null)
                model.addAttribute(LIST_OF_BLOGS_KEY, listOfBlogs);
            return SHOW_POSTS_PAGE;
        }
    }

    private String processCannotPost(String blogID, ModelMap model, String userName, ApplicationContext appContext,
                                     String content) {
        BlogDAO editPost = (BlogDAO) appContext.getBean(BLOG_DAO_BEAN_NAME);
        Blog thePost = editPost.getaBlog(Integer.parseInt(blogID));
        if(thePost.getAuthor().equals(userName)) {
            thePost.setContent(content);
            model.addAttribute(LOGGED_IN_NAME_KEY, userName);
            model.addAttribute(POST_TO_EDIT_KEY, thePost);
            model.addAttribute(ERROR_MESSAGE_KEY, POST_ADD_OR_MODIFY_QUICKLY);
            ((ConfigurableApplicationContext) appContext).close();
            return EDIT_POST_PAGE;
        }
        else {
            model.addAttribute(ERROR_MESSAGE_KEY, NOT_ABLE_TO_RETRIEVE_POST_ERROR);
            goToAllPostsPage(appContext, model);
            return SHOW_POSTS_PAGE;
        }
    }

    private boolean checkBlogIDValue(String BlogID) {
        int blogIDValue = Integer.parseInt(BlogID);
        return (blogIDValue > 0 && blogIDValue < Integer.MAX_VALUE);
    }
}
