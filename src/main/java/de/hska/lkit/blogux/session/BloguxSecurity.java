package de.hska.lkit.blogux.session;

import org.springframework.core.NamedThreadLocal;

public abstract class BloguxSecurity {

  private static final ThreadLocal<UserInfo> user = new NamedThreadLocal<UserInfo>("blogux-id");

  private static class UserInfo {
    String name;
    String uid;
  }

  public static void setUser(String name, String uid) {
    System.out.printf("In set user: name=%s, uid=%s\n", name, uid);

    UserInfo userInfo = new UserInfo();
    userInfo.name = name;
    userInfo.uid = uid;
    user.set(userInfo);
    System.out.println("check");
    System.out.println(user.get().name);
  }

  public static boolean isSignedIn() {
    UserInfo userInfo = user.get();
    if ( userInfo != null) {
      return true;
    }
    return false;
  }

  public static boolean isUserSignedIn(String name) {
    UserInfo userInfo = user.get();
    return userInfo != null && userInfo.name.equals(name);
  }

  public static String getName() {

    System.out.println("GETNAME");
    System.out.println("get nam"+user.get().name);
    UserInfo userInfo = user.get();
    System.out.println("userinf"+userInfo.name);


    return userInfo.name;
  }

  public static String getUid() {
    UserInfo userInfo = user.get();
    return userInfo.uid;
  }

}
