package jess;
//number 07
import javax.jdo.PersistenceManager;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Datastore {	
	private static PersistenceManager pm;
	// Writes and updates  items that are in the datastore	 
	public static void writeElement(Object element){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		 }
		pm.makePersistent(element);
		System.out.println("[Datastore][WRITE] - element has been written to database, classtype: "+element.getClass().getSimpleName());
		}
	 //Deletes item in datastore
	public static void deleteElement(Object element){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
		pm.deletePersistent(element);
		System.out.println("[Datastore][DELETE] - the element has been removed from the database The Classtype: "+element.getClass().getSimpleName());
	}
	// Reads the dropbox directory	 
	public static DropboxDirectory readDropboxDirectory(User u, String path){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
		  if (u == null) {
			System.err.println("[Datastore][READ][Directory] - cant load this directory: "
					+ path + " google user is null");
			    return null;
		     }
		Key directory_key = KeyFactory.createKey("DropboxDirectory",u.getUserId() + path);	
		DropboxDirectory directory = null;
		try {
			directory = pm.getObjectById(DropboxDirectory.class, directory_key);
		} catch (Exception e) {
			
			System.err.println("[Datastore][READ][Directory] - cant load this directory: "
					+ path);
			return null;
		}
		return directory;
	}
	 //reads the dropbox user
	 public static DropboxUser readDropboxUser(User u){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
	      if (u == null) {
			System.err.println("[Datastore][READ][User] - cant load the user since the google user is null");
			return null;
		}
		Key user_key = KeyFactory.createKey("DropboxUser", u.getUserId());
		DropboxUser du = null;		
		try {
			du = pm.getObjectById(DropboxUser.class, user_key);	
		} 
		  catch (Exception e) {
			System.err.println("[Datastore][READ][User] - cant load the user");
			return null;
		}
     	return du;
	}
	//commit
	public static void commit(){
		if(pm != null){
			pm.close();
			pm = null;
		}
	  }  	
    }