package de.hska.lkit.blogux.repo;

import de.hska.lkit.blogux.places.Login;
import java.util.Map;

import de.hska.lkit.blogux.model.User;

public interface UserRepository {

	public void createUser(Login user);
	public void saveUser(User user);
	public Map<String, User> getAllUsers();
	public User getUser(String username);
}
