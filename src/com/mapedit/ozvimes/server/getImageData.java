package com.mapedit.ozvimes.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.ozvimes.AreaData;
import com.common.ozvimes.Item;
import com.common.ozvimes.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class getImageData extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try{
			if( req.getParameter("type").equals("map") ){
				String areaKey = req.getParameter("areaKey");

				String name = req.getParameter("name");
				double xpos = Double.valueOf(req.getParameter("xpos"));
				double ypos = Double.valueOf(req.getParameter("ypos"));
				double meter = Double.valueOf(req.getParameter("meter"));
				double OWS = Double.valueOf(req.getParameter("OWS"));
				Boolean onChange = Boolean.valueOf(req.getParameter("onChangeMap"));

				Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
				List<BlobKey> blobKey = blobs.get("map");
				BlobKey bkey = blobKey.get(0);


				//データベース全部読み込み
				String query = "select from " + AreaData.class.getName();
				List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();
				
				int i=0;
				for ( i=0; i < datas.size(); i++ ) {
					if( datas.get(i).getKey().getId() == Long.valueOf(areaKey) ){
						
						datas.get(i).setX(xpos);
						datas.get(i).setY(ypos);
						datas.get(i).setM(meter);
						datas.get(i).setOWS(OWS);
						
						if(! datas.get(i).getPath().equals("none") && onChange){
							BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();
							BlobKey blobDel = new BlobKey( datas.get(i).getPath() );
							blobService.delete(blobDel);
							
							datas.get(i).setPath(bkey.getKeyString());
						} else if(datas.get(i).getPath().equals("none") && onChange){
							datas.get(i).setPath(bkey.getKeyString());
						}
					}
				}


				//InMap emp = new InMap( name, xpos, ypos, meter, OWS, bkey.getKeyString());


				//try {
				// オブジェクトの格納
				//	pm.makePersistent(emp);

				//			} finally {
				//pm.close();
				//		}
			}


			if( req.getParameter("type").equals("icon") ){
				String itemkey = req.getParameter("itemkey");

				Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
				List<BlobKey> blobKey = blobs.get("icon");
				BlobKey bkey = blobKey.get(0);
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

				//pm.close();
			}
		}
		finally{

			pm.close();
		}
	}
}
