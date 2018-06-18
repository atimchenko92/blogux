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
  private String srcPattern;
  private int pagesAmount;

  public Home() {
    this.activetab = "timeline-my";
    this.isself = true;
    this.pagesAmount = 1;
  }

  public Home(User currentUser) {
    setCurrentUser(currentUser);
    this.activetab = "timeline-my";
    this.pagesAmount = 1;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public boolean getIsself() {
    return isself;
  }

  public void setIsself(boolean isSelf) {
    this.isself = isSelf;
  }

  public void setActivetab(String activetab) {
    this.activetab = activetab;
  }

  public String getActivetab() {
    return activetab;
  }

  public void setSrcPattern(String pattern) {
    this.srcPattern = pattern;
  }

  public String getSrcPattern() {
    return srcPattern;
  }

  public void setPagesAmount(int pagesAmount) {
    this.pagesAmount = pagesAmount;
  }

  public int getPagesAmount() {
    return pagesAmount;
  }
}
