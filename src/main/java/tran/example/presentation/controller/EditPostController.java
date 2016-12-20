package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.service.EditPostControllerService;

import java.security.Principal;

@Controller
public class EditPostController {

	private static final String SHOW_EDIT_POST_MAPPING = "/showEditPost";

	private static final String BLOG_ID_PARAM = "blogID";

	private static final String CONTENT_PARAM = "content";

	@Autowired
    EditPostControllerService editPostControllerService;

	@RequestMapping(value=SHOW_EDIT_POST_MAPPING, method=RequestMethod.GET)
	public String showEditPost(@RequestParam(value=BLOG_ID_PARAM, required=false) String blogID, Principal principal,
							   ModelMap model) {
		return editPostControllerService.showEditPost(blogID, principal, model);
	}

	@RequestMapping(value = "/processEditPost", method = RequestMethod.POST)
	public String processEditPost(@RequestParam(value=BLOG_ID_PARAM, required = false) String blogID,
								  @RequestParam(value=CONTENT_PARAM, required = false) String content,
								  Principal principal, ModelMap model) {
		return editPostControllerService.processEditPost(blogID, content, principal, model);
	}
		
}
