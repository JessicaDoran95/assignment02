package jess;
//NUMBER07
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jdo.PersistenceManager;

@SuppressWarnings("serial")
public class DirectoryServlet extends DropboxServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String value = req.getParameter("Button");
		  if (value == null) {	
			System.err.println("[DirectoryServlet] - this is an invalid value for the button determination");	
		} 
		    else if (value.compareToIgnoreCase("add") == 0) {
			System.out.println("[DirectoryServlet] - the add button has been hit");
			addDirectory(req);
		} 
		  else if (value.compareToIgnoreCase("change") == 0) {

			System.out.println("[DirectoryServlet] - the change button has been hit");
			changeDirectory(req);
		} 
		  else if (value.compareToIgnoreCase("delete") == 0) {

			System.out.println("[DirectoryServlet] - delete button has been pressed");
			deleteDirectory(req);
		}
		resp.sendRedirect("/");
	}
	private void addDirectory(HttpServletRequest req) {
		//DIRECTORYNAME
		String newDirectoryName = req.getParameter("textfield");
		newDirectoryName = newDirectoryName.trim().replace("/", "") + "/";
		System.out.println("[addDirectory] - the user wants to add "+newDirectoryName);
		//LOAD THE CURRENTDIRECTORY
		DropboxDirectory currentDirectory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
		//check for directories existence
		    if (!currentDirectory.subdirExists(newDirectoryName)) {	
			//if it is then  ADD
			currentDirectory.addSubDir(newDirectoryName);
			//  then need to genereate
			DropboxDirectory newDirectory = generateDirectory(getCurrentPath(req) + newDirectoryName);
			// then update the parent
			Datastore.writeElement(currentDirectory);
			//then update the directory
			Datastore.writeElement(newDirectory);
		}
		Datastore.commit();
	}
	      private void changeDirectory(HttpServletRequest req) {
		String targetDirectory = req.getParameter("key_value");
		if (targetDirectory.compareTo("../") == 0) {
			String path = getCurrentPath(req).trim();
			String[] split = path.split("/");
         	String parentPath = "";
			for (int i = 1; i < split.length - 1; i++) {
				parentPath += "/" + split[i];
		    }
			parentPath += "/";
			System.out.println("[changeDirectory] - user wants to change the directory to "+parentPath);
			setCurrentPath(req, parentPath);
		} else {	
			System.out.println("[changeDirectory] - user wants to change the directory to "+getCurrentPath(req) + targetDirectory);
			setCurrentPath(req, getCurrentPath(req) + targetDirectory);
		}
	}
	      //if delete should delete the directory
	    private void deleteDirectory(HttpServletRequest req) {
		String targetDirectory = req.getParameter("key_value");
		System.out.println("[deleteDirectory] - The user wants to delete this directory "+getCurrentPath(req)
			+ targetDirectory);
		DropboxDirectory directory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req)+targetDirectory);
		//is subdirectory empty
			 if (directory.isEmpty()) {
				Datastore.deleteElement(directory);
				DropboxDirectory parentDirectory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
				parentDirectory.deleteSubDir(targetDirectory);
			Datastore.writeElement(parentDirectory);
		  }
		Datastore.commit();
	  }
  }