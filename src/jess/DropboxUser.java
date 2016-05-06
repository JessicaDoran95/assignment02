package jess;
//NUMBER 04
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;
//KEY THAT IDENTIFIES THE USER
@PersistenceCapable
public class DropboxUser {
 @PrimaryKey
 @Persistent
 private Key id;	
  public DropboxUser (Key id){	
		this.id = id;
      }
    }
