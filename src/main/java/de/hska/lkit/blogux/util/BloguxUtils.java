package de.hska.lkit.blogux.util;

import de.hska.lkit.blogux.model.Post;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class BloguxUtils {

  private static final int MAX_USERS_IN_PAGE = 8;
  private static final int MAX_POSTS_IN_PAGE = 5;

  public static final List<String> getUserListByPage(int page, Set<String> userSet) {
    List<String> userList = new ArrayList<String>();
    for (String usr : userSet)
      userList.add(usr);
    Collections.sort(userList);

    int idxFrom = (page * MAX_USERS_IN_PAGE > userList.size()) ? userList.size() : page * MAX_USERS_IN_PAGE;
    int idxTill = (page * MAX_USERS_IN_PAGE + MAX_USERS_IN_PAGE > userList.size()) ? userList.size()
        : page * MAX_USERS_IN_PAGE + MAX_USERS_IN_PAGE;

    return userList.subList(idxFrom, idxTill);
  }

  public static final List<Post> getTimelinePostsByPage(int page, List<Post> postList) {

    int idxFrom = (page * MAX_POSTS_IN_PAGE > postList.size()) ? postList.size() : page * MAX_POSTS_IN_PAGE;
    int idxTill = (page * MAX_POSTS_IN_PAGE + MAX_POSTS_IN_PAGE > postList.size()) ? postList.size()
        : page * MAX_POSTS_IN_PAGE + MAX_POSTS_IN_PAGE;

    return postList.subList(idxFrom, idxTill);
  }

  public static final int getUserlistPagesNumber(Set<String> userSet) {
    int pg = userSet.size() % BloguxUtils.MAX_USERS_IN_PAGE == 0 ? 0 : 1;
    return userSet.size() / BloguxUtils.MAX_USERS_IN_PAGE + pg;
  }

  public static final int getTimelinePagesNumber(List<Post> postList) {
    int pg = postList.size() % BloguxUtils.MAX_POSTS_IN_PAGE == 0 ? 0 : 1;
    return postList.size() / BloguxUtils.MAX_POSTS_IN_PAGE + pg;
  }

}
