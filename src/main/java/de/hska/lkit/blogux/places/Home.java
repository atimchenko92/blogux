package de.hska.lkit.blogux.places;

import de.hska.lkit.blogux.model.User;
/**
 * @author atimchenko
 *
 */
public class Home {
  private String activeTab;
  private User currentUser;

  public Home() {
  }

  public Home(User currentUser) {
    this.currentUser = currentUser;
    this.activeTab = "test";
  }

  public String getUserName() {
    return currentUser.getUsername();
  }

  public String getFirstName() {
    return currentUser.getUsername();
  }

  public void setActiveTab(String activeTab){
    this.activeTab = activeTab;
  }

  public String getActiveTab(){
    return activeTab;
  }
}
