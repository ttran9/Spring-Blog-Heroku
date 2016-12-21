package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tran.example.service.ShowPostsControllerService;

import java.security.Principal;

@Controller
public class ShowPostsController {

	private static final String SHOW_POSTS_MAPPING = "/";

	@Autowired
    ShowPostsControllerService showPostsControllerService;

	@RequestMapping(value=SHOW_POSTS_MAPPING, method={RequestMethod.GET, RequestMethod.POST})
	public String showPosts(Principal principal, ModelMap model) {
		return showPostsControllerService.showPosts(principal, model);
	}
}
