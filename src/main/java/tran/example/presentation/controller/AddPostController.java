package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.service.AddPostControllerService;

import java.security.Principal;

/**
 * @Author Todd
 * This class provides the mapping for a user to be able to view the web pages to create a post.
 */
@Controller
public class AddPostController {

	@Autowired
	private AddPostControllerService addPostControllerService;

	private static final String ADD_POST_MAPPING = "/addPost";

	private static final String PROCESS_ADD_POST_MAPPING = "/processAddPost";

	private static final String TITLE_PARAM = "title";

	private static final String CONTENT_PARAM = "content";


	@RequestMapping(value=ADD_POST_MAPPING, method=RequestMethod.GET)
	public String displayAddForm(Principal principal, ModelMap model) {
		return addPostControllerService.displayAddForm(principal, model);
	}
	
	@RequestMapping(value=PROCESS_ADD_POST_MAPPING, method=RequestMethod.POST)
	public String processAddForm(@RequestParam(value=TITLE_PARAM, required=false) String title,
								 @RequestParam(value=CONTENT_PARAM, required=false) String content, Principal principal,
								 ModelMap model) {
		return addPostControllerService.processAddForm(title, content, principal, model);
	}
}
