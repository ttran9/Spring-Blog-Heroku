package tran.example.presentation.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.data.BlogDAO;
import tran.example.data.UserDAO;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Controller
public class AddPostController {

	@RequestMapping(value="/addPost", method=RequestMethod.GET)
	public String displayAddForm(Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				model.addAttribute("loggedInName", userName);
				return "addPost";
			}
			else {
				model.addAttribute("error", "You must be logged in before viewing this page.");
		    	return "signin";
			}
		}
		else {
			model.addAttribute("error", "You must be logged in before viewing this page.");
	    	return "signin";
		}
	}
	
	@RequestMapping(value = "/processAddPost", method = RequestMethod.POST)
	public String processAddForm(@RequestParam(value = "title", required = false) String title, 
			@RequestParam(value = "content", required = false) String content, Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
				UserDAO userDAO = (UserDAO)appContext.getBean("userDS");
				if(userDAO.canUserPost(userName)) {
					BlogDAO getPosts = (BlogDAO) appContext.getBean("BlogDS");
					((ConfigurableApplicationContext) appContext).close();
					int createPostMessage = getPosts.addBlog(title, content, userName);
					if(createPostMessage != -1) {
						LocalDateTime current_time = Timestamp.from(Instant.now()).toLocalDateTime();
						userDAO.updateLastPostedTimeForUser(current_time, userName);
					}
					return "redirect:showSinglePost?blogID=" + createPostMessage;
				}
				else {
					model.addAttribute("entered_title", title);
					model.addAttribute("errorMessage", "You must wait 30 seconds from editing or posting to add another post.");
					model.addAttribute("entered_content", content);
					model.addAttribute("loggedInName", userName);
					return "addPost";
				}
			}
			else {
				model.addAttribute("error", "You must be logged in before creating a post.");
		    	return "signin";
			}
		}
		else {
			model.addAttribute("error", "You must be logged in before creating a post.");
	    	return "signin";
		}
	}
}
