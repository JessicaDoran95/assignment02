package jess;
import javax.servlet.http.HttpServlet;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.*;
//num 6
@SuppressWarnings("serial")
public abstract class DropboxServlet extends HttpServlet {
 protected User getCurrentUser() {
    UserService us = UserServiceFactory.getUserService();
		return us.getCurrentUser();
	}
  protected boolean isCurrentUser() {
	return getCurrentUser() != null;
	}
// This returns currentPath if it is set
  protected String getCurrentPath(HttpServletRequest req) {
	String currentPath = (String) req.getSession().getAttribute("CurrentPath");
		 if (currentPath != null && currentPath.length() > 0) {
			System.out.println("[getCurrentPath] - Current Path (contained in the Cookie): "+currentPath);
	} 	 
	 else {
		currentPath = "/";
		System.out.println("[getCurrentPath] - Current Path (NOT contained in the Cookie): "+currentPath);
		req.getSession().setAttribute("currentPath", "/");
	 }
		return currentPath;
	}
   protected void setCurrentPath(HttpServletRequest req, String path) {
		System.out.println("[setCurrentPath] - Current Path is set to the Cookie: "+path);
		req.getSession().setAttribute("currentPath", path);
	}
   protected DropboxDirectory generateDirectory(String path) {
		if (!isCurrentUser()) {
			   System.err.println("[generateDirectory] - cant generate the directory: "
							+ path + " user is not logged in");
			return null;
		}
	Key directory_key = KeyFactory.createKey("DropboxDirectory", getCurrentUser().getUserId() + path);
	DropboxDirectory directory = new DropboxDirectory(directory_key, path);
	System.out.println("[generateDirectory] - Directory is generated: "+path);
	return directory;
	}
}

