package de.hska.lkit.blogux.places;

import de.hska.lkit.blogux.model.User;
/**
 * @author atimchenko
 *
 */
public class Home {
  private String activetab;
  private User currentUser;
  private boolean isself;

  public Home() {
    this.activetab = "timeline-my";
    this.isself = true;
  }

  public Home(User currentUser) {
    this.currentUser = currentUser;
    this.activetab = "timeline-my";
  }

  public boolean getIsself(){
    return isself;
  }

  public void setIsself(boolean isSelf){
    this.isself = isSelf;
  }

  public String getUserName() {
    return currentUser.getUsername();
  }

  public String getFirstName() {
    return currentUser.getUsername();
  }

  public void setActivetab(String activetab){
    this.activetab = activetab;
  }

  public String getActivetab(){
    return activetab;
  }
}
