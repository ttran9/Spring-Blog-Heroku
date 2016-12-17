package tran.example.presentation.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tran.example.presentation.model.Blog;
import tran.example.dao.BlogDAO;

@Controller
public class ShowPostsController {

	@RequestMapping(value="/showPosts", method={RequestMethod.GET, RequestMethod.POST})
	public String showForm(Principal principal, ModelMap model) {
		if(principal != null) {
			String userName = principal.getName();
			if(userName != null) {
				model.addAttribute("loggedInName", userName);
			}
		}
		ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
		BlogDAO getPosts = (BlogDAO)appContext.getBean("BlogDS");
		List<Blog> listOfBlogs = getPosts.getBlogs();
		((ConfigurableApplicationContext)appContext).close();
		if(listOfBlogs.size() > 0)
			model.addAttribute("blogs", listOfBlogs);

		return "showPosts";
	}
}
