package jess;
//NUMBER03
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.blobstore.BlobKey;
//constructer
@PersistenceCapable
public class DropboxFile {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key id; 
  @Persistent
  private String name;
  @Persistent
  private DropboxDirectory owner;
  @Persistent
  	//BLOB KEY HERE
	private BlobKey blob;
	public DropboxFile (String name, BlobKey blob, DropboxDirectory owner){
		this.name = name;
		this.blob = blob;
		this.owner = owner;
	   }
	//GETNAME
	public String getName() {
		return name;
	}
	//blobkey
	public BlobKey getBlobKey() {
		return blob;
	}
}