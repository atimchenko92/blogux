package de.hska.lkit.blogux.controller;

import de.hska.lkit.blogux.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * @author atimchenko
 *
 */
@Controller
public class PostController {

	private final PostRepository postRepository;

	@Autowired
	public PostController(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}

}
