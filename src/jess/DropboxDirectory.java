package jess;

import java.util.ArrayList;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;


import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DropboxDirectory {

	@PrimaryKey
	  @Persistent
	   private Key id;
	
	   @Persistent
	   private String dirName;

	    @Persistent
	    private ArrayList<String> subDirectories;
	}
