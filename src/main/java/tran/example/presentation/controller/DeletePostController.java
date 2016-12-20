package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.service.DeletePostControllerService;

import java.security.Principal;

@Controller
public class DeletePostController {

	private static final String DELETE_POST_MAPPING = "/deleteSinglePost";

	private static final String BLOG_ID_PARAM = "blogID";

	@Autowired
	private DeletePostControllerService deletePostControllerService;

	@RequestMapping(value=DELETE_POST_MAPPING, method=RequestMethod.GET)
	public String processDeletePost(@RequestParam(value=BLOG_ID_PARAM, required=false) String blogID,
									Principal principal, ModelMap model) {
		return deletePostControllerService.checkIfPostExists(blogID, principal) ?
				deletePostControllerService.processDeletePost(blogID, principal, model) :
				deletePostControllerService.displayAllPosts(model, principal);
	}
}
