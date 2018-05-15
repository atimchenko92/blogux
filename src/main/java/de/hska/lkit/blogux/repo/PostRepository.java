package de.hska.lkit.blogux.repo;

import java.util.List;
import java.util.Map;
import de.hska.lkit.blogux.model.Post;

public interface PostRepository {

	public void savePost(Post post);
	public Post getPost(String id);
	public Map<String, Post> getAllPosts();

	public List<Post> getUserPostsInRange(String username, long start, long end);
	public List<Post> getGlobalPostsInRange(long start, long end);

	public List<Post> prepareFollowingPosts(String username);
	public List<Post> preparePersonalAndFollowingPosts(String username);

}
