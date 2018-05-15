package de.hska.lkit.blogux.repo;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import de.hska.lkit.blogux.places.Login;
import java.util.Map;

import de.hska.lkit.blogux.model.User;

public interface UserRepository {

	public void createUser(Login user);
	public void saveUser(User user);
	public Map<String, User> getAllUsers();
	public User getUser(String username);
	public User getUserByCookie(HttpServletRequest req);
	public void followUnfollow(User currentUser, User inspectedUser);
	public boolean checkIfFollows(User currentUser, User inspectedUser);
	public boolean userAlreadyExists(String username);
	public Set<String> getUserFollowers(User currentUser);
	public Set<String> getUserFollows(User currentUser);
	public Set<String> getFollowers(String username);
	public Set<String> getFollows(String username);
	public Set<String> getSearchResults(String pattern);
}
