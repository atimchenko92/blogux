package de.hska.lkit.blogux.repo;

import java.util.Map;

import de.hska.lkit.blogux.model.Post;

public interface PostRepository {

	public void savePost(Post post);
	public Post getPost(String id);
	public Map<String, Post> getAllPosts();

}
