package tran.example.presentation.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.presentation.model.Blog;
import tran.example.data.BlogDAO;
import tran.example.data.UserDAO;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EditPostController {
	@RequestMapping(value = "/showEditPost", method = RequestMethod.GET)
	public String showEditPost(@RequestParam(value = "blogID", required = false) String blogID, @RequestParam(value = "content", required = false) String content, Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
				if(blogID != null && !blogID.equals("-1") && !blogID.equals("0")) {
					BlogDAO getPost = (BlogDAO)appContext.getBean("BlogDS");
					Blog thePost = getPost.getaBlog(Integer.parseInt(blogID));
					((ConfigurableApplicationContext)appContext).close();
					if(!(thePost.getContent().equals("")) && getPost.isAuthorOfPost(userName, Integer.parseInt(blogID))) {
						model.addAttribute("loggedInName", userName);
						model.addAttribute("postToEdit", thePost);
						return "editPost";
					}
					else {
						model.addAttribute("error", "You must be the author of a post to edit it.");
						goToAllPostsPage(appContext, model);
						return "showPosts";
					}
				}
				else {
					model.addAttribute("error", "The post could not be edited, make sure you are passing in the proper blog ID.");
					goToAllPostsPage(appContext, model);
					return "showPosts";
				}
			}
		}
		model.addAttribute("error", "You must be logged in before editing a post.");
    	return "signin";
	}
	
	public void goToAllPostsPage(ApplicationContext appContext, ModelMap model) {
		BlogDAO getPosts = (BlogDAO)appContext.getBean("BlogDS");
		List<Blog> listOfBlogs = getPosts.getBlogs();
		((ConfigurableApplicationContext)appContext).close();
		if(listOfBlogs.size() > 0)
			model.addAttribute("blogs", listOfBlogs);
	}
	
	
	@RequestMapping(value = "/processEditPost", method = RequestMethod.POST)
	public String processEditPost(@RequestParam(value = "blogID", required = false) String blogID, @RequestParam(value = "content", required = false) String content, Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
				UserDAO userDAO = (UserDAO)appContext.getBean("userDS");
				if(userDAO.canUserPost(userName)) {
					if (blogID != null && !blogID.equals("-1") && !blogID.equals("0")) {
						BlogDAO editPost = (BlogDAO) appContext.getBean("BlogDS");
						// edit the post.
						String updatePostMessage = editPost.updatePost(Integer.parseInt(blogID), content);
						Blog newPost = editPost.getaBlog(Integer.parseInt(blogID));
						((ConfigurableApplicationContext) appContext).close();
						if (newPost != null) {
							model.addAttribute("error", updatePostMessage);
							model.addAttribute("loggedInName", userName);
							model.addAttribute("postContents", newPost);
							boolean isAuthorOfPost = editPost.isAuthorOfPost(userName, Integer.parseInt(blogID));
							model.addAttribute("isAuthorOfPost", isAuthorOfPost);
							LocalDateTime current_time = Timestamp.from(Instant.now()).toLocalDateTime();
							userDAO.updateLastPostedTimeForUser(current_time, userName);
							return "showSinglePost";
						} else {
							// since the post cannot be found do not pass in a blogID parameter.
							return "redirect:/showSinglePost";
						}
					} else {
						model.addAttribute("error", "The post could not be edited, make sure you are passing in the proper blog ID.");
						BlogDAO getPosts = (BlogDAO) appContext.getBean("BlogDS");
						List<Blog> listOfBlogs = getPosts.getBlogs();
						((ConfigurableApplicationContext) appContext).close();
						if (listOfBlogs.size() > 0)
							model.addAttribute("blogs", listOfBlogs);
						return "showPosts";
					}
				}
				else {
					BlogDAO editPost = (BlogDAO) appContext.getBean("BlogDS");
					Blog thePost = editPost.getaBlog(Integer.parseInt(blogID));
					if(thePost.getAuthor().equals(userName)) {
						thePost.setContent(content);
						model.addAttribute("loggedInName", userName);
						model.addAttribute("postToEdit", thePost);
						model.addAttribute("errorMessage", "You must wait at least 30 seconds from your last edit or last posted time to edit a post.");
						return "editPost";
					}
					else {
						model.addAttribute("error", "Could not retrieve the post you are attempting to edit.");
						goToAllPostsPage(appContext, model);
						return "showPosts";
					}
				}
			}
			else {
				model.addAttribute("error", "You must be logged in and the author of the post to edit the post.");
		    	return "signin";
			}
		}
		else {
			model.addAttribute("error", "You must be logged in before editing a post.");
	    	return "signin";
		}
	}
		
}
