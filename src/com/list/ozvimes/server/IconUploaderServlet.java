package com.list.ozvimes.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.ozvimes.Item;
import com.common.ozvimes.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class IconUploaderServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		String itemkey = req.getParameter("itemkey");

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKey = blobs.get("icon");
		BlobKey bkey = blobKey.get(0);


		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read

		//データベース全部読み込み
		String query = "select from " + Item.class.getName();
		List<Item> datas = (List<Item>) pm.newQuery(query).execute();
		
		int i=0;
		for ( i=0; i < datas.size(); i++ ) {
			if( datas.get(i).getKey().getId() == Long.valueOf(itemkey) ){
				
				Item cItem = pm.getObjectById(Item.class, datas.get(i).getKey().getId());
				
				cItem.setIconUrl( bkey.getKeyString() ) ;
			}
		}

		//		InMap emp = new InMap( name, xpos, ypos, meter, OWS, bkey.getKeyString());

		pm.close();
	}
}
