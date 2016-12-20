package tran.example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.service.ShowSinglePostControllerService;

import java.security.Principal;

@Controller
public class ShowSinglePostController {

	private static final String SHOW_SINGLE_POST_MAPPING = "/showSinglePost";

	private static final String BLOG_ID_PARAM = "blogID";

	@Autowired
    ShowSinglePostControllerService showSinglePostControllerService;

	@RequestMapping(value={SHOW_SINGLE_POST_MAPPING}, method=RequestMethod.GET)
	public String showSinglePost(@RequestParam(value=BLOG_ID_PARAM, required=false) Integer blogID,
								 Principal principal, ModelMap model) {
		return showSinglePostControllerService.showSinglePost(blogID, principal, model);
	}
}
