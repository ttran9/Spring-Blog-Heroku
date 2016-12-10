package tran.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tran.example.model.Blog;
import tran.example.model.DAO.BlogDAO;

@Controller
public class DeletePostController {
	
	private final static String DELETE_POST_SUCCESS = "post successfully deleted!";

	@RequestMapping(value = "/deleteSinglePost", method = RequestMethod.GET)
	public String processDeletePost(@RequestParam(value = "blogID", required = false) String blogID, 
			@RequestParam(value = "content", required = false) String content, Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
				if(blogID != null && !blogID.equals("-1") && !blogID.equals("0")) {
					BlogDAO deletePost = (BlogDAO)appContext.getBean("BlogDS");
					String deletePostMessage = deletePost.removePost(Integer.parseInt(blogID), userName);
					if(deletePostMessage.equals(DELETE_POST_SUCCESS)) {
						model.addAttribute("error", deletePostMessage);
						
						BlogDAO getPosts = (BlogDAO)appContext.getBean("BlogDS");
						List<Blog> listOfBlogs = getPosts.getBlogs();
						((ConfigurableApplicationContext)appContext).close();
						if(listOfBlogs.size() > 0)
							model.addAttribute("blogs", listOfBlogs);
						
						return "showPosts";
					}
					else {
						model.addAttribute("error", deletePostMessage);
						((ConfigurableApplicationContext)appContext).close();
						return "showSinglePost";
					}
				}
				else {
					model.addAttribute("error", "The post could not be deleted, make sure you are passing in the proper blog ID.");
					BlogDAO getPosts = (BlogDAO)appContext.getBean("BlogDS");
					List<Blog> listOfBlogs = getPosts.getBlogs();
					((ConfigurableApplicationContext)appContext).close();
					if(listOfBlogs.size() > 0)
						model.addAttribute("blogs", listOfBlogs);
					((ConfigurableApplicationContext)appContext).close();
					return "showPosts";
				}
			}
			else {
				model.addAttribute("error", "You must be logged in and the author of the post to delete a post.");
		    	return "signin";
			}
		}
		else {
			model.addAttribute("error", "You must be logged in before deleting a post.");
	    	return "signin";
		}
	}
}
