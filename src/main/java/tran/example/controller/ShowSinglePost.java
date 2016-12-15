package tran.example.controller;

import java.security.Principal;

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
public class ShowSinglePost {
	@RequestMapping(value={"/showSinglePost"}, method=RequestMethod.GET)
	public String showForm(@RequestParam(value = "blogID", required = false) Integer blogID, Principal principal, ModelMap model) {
		if(blogID == null) {
			model.addAttribute("error", "you must provide a valid blog ID number.");
		}
		else {
			// open a connection to the database and get the necessary info
			ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
			BlogDAO getPost = (BlogDAO)appContext.getBean("BlogDS");
			Blog singlePost = getPost.getaBlog(blogID);
			((ConfigurableApplicationContext)appContext).close();
			if(singlePost != null) {
				// check if a user is logged in and if so then check if the user is the author of this post.
				if (principal != null) {
					String userName = principal.getName();
					if (userName != null) {
						model.addAttribute("loggedInName", userName);
						Boolean isAuthorOfPost = getPost.isAuthorOfPost(userName, blogID);
						model.addAttribute("isAuthorOfPost", isAuthorOfPost);
					}
				}
				if (singlePost.getAuthor() == null && singlePost.getContent() == null && singlePost.getTitle() == null /* && singlePost.getDateCreated() == null && singlePost.getDateModified() == null*/) {
					model.addAttribute("error", "A blog with that id could not be found.");
				} else {
					model.addAttribute("postContents", singlePost);
				}
			}
		}
		return "showSinglePost";
	}
}
