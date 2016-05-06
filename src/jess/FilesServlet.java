package jess;
//NUMBER 08
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;

@SuppressWarnings("serial")
public class FilesServlet extends DropboxServlet {
public void doPost(HttpServletRequest req, HttpServletResponse resp)
throws IOException {		
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		try{
		Map<String, List<BlobKey>> files_sent = blobstoreService.getUploads(req);
		BlobKey b = files_sent.get("file").get(0);
		String fileName = blobstoreService.getBlobInfos(req).get("file").get(0).getFilename();
				DropboxDirectory dir = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));	
				DropboxFile file = new DropboxFile(fileName, b, dir);
				dir.addFile(file);		
				Datastore.writeElement(file);
				Datastore.writeElement(dir);
				Datastore.commit();
		 }
		catch (Exception e){
		System.out.println("error");
		}
		resp.sendRedirect("/");
	   }
//determinung if delete/download button has been clicked
public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws IOException, ServletException {
	String value = req.getParameter("Button");
		if (value == null) {
			System.err.println("[FileServlet] - this is an invalid value for button determination");
			resp.sendRedirect("/");
		} 
		    else if (value.compareToIgnoreCase("delete") == 0) {
			System.out.println("[FileServlet] - the delete button has been clicked");
			deleteFile(req);
			resp.sendRedirect("/");
		} else if (value.compareToIgnoreCase("download") == 0) {
			System.out.println("[FileServlet] - the download button has been clicked");
			downloadFile(req, resp);

		}
	}

 private void deleteFile(HttpServletRequest req) {	
	 String fileNameString = req.getParameter("key_value");
		DropboxDirectory directory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
		DropboxFile file = null;
		List<DropboxFile> files = directory.files();
		  for (int i = 0; i < files.size(); i++) {
			   if (files.get(i).getName().compareTo(fileNameString) == 0) {
				   file = files.get(i);
			    }
		   }
		     if(file == null){
			     System.out.println("[deleteFile] - "+fileNameString+"...has not been found");
		     }
		     //The delete file
		BlobstoreServiceFactory.getBlobstoreService().delete(file.getBlobKey());
		directory.deleteFile(file);
		Datastore.writeElement(directory);
		Datastore.commit();
		System.out.println("[deleteFile] - "+fileNameString+"... has been deleted from "+directory.getDirName());
	}

 private void downloadFile(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		String fileNameString = (String) req.getParameter("key_value");
		DropboxDirectory directory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));

		List<DropboxFile> files = directory.files();
		  for (int i = 0; i < files.size(); i++) {
			 if (files.get(i).getName().compareTo(fileNameString) == 0) {
				//These are the blob keys
				BlobKey key = files.get(i).getBlobKey();
				BlobstoreServiceFactory.getBlobstoreService().serve(
						key, resp);
				System.out.println("[downloadFile] - "+fileNameString+"... is downloaded by user");
				return;
			}
		  }
		Datastore.commit();
		System.out.println("[downloadFile] - "+fileNameString+"... has not been found!");
	}
}

