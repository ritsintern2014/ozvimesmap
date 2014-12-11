package com.mapedit.ozvimes.server;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.common.ozvimes.AreaData;
import com.common.ozvimes.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mapedit.ozvimes.client.MapEditService;

public class MapEditServiceImpl extends RemoteServiceServlet implements MapEditService {
	public String echoBack(final String name, final double x, final double y, final double meter, final double OWS){


		final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		final String uploadUrl = blobstoreService.createUploadUrl("/upload");
		/*
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "/upload");
        //builder.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try {
            builder.sendRequest("", new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                   // TODO Auto-generated method stub
                	Map<String, List<BlobKey>> blobs = blobstoreService.getUploads((HttpServletRequest) request);
                	List<BlobKey> blobKeyList = blobs.get(0);

                	if (blobKeyList.size() == 0)
                	return;

                	BlobKey blobKey = blobKeyList.get(0);

                	//InMap.setPath(uploadUrl);


            		InMap emp = new InMap( name, x, y, meter, OWS, blobKey.getKeyString());

            		// PersistenceManager の作成
            		PersistenceManager pm = PMF.get().getPersistenceManager();

            		try {
            			// オブジェクトの格納
            			pm.makePersistent(emp);

            		} finally {
            			pm.close();
            		}

            		//            		blobstoreService.getUploadedBlobs();

            		//return uploadUrl;
                }

                @Override
                public void onError(Request request, Throwable exception) {
                	Window.alert("error");
                }

            });

        } catch (RequestException e) {
        	Window.alert("error2");
        }*/


		//		InMap emp = new InMap( name, x, y, meter, OWS, uploadUrl);

		return uploadUrl;
	}



	public String getMapData(String name){
		String str = "";

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String mq = "select from " + AreaData.class.getName();
		List<AreaData> areaDatas = (List<AreaData>) pm.newQuery(mq).execute();

		int i = 0;
		for(i=0; i<areaDatas.size(); i++){
			if(areaDatas.get(i).getKey().getId() == Long.parseLong(name) ){
				if(areaDatas.get(i).getPath().equals("none")){
					str = "none";
				} else{
					ImagesService imagesService = ImagesServiceFactory.getImagesService();
					
					BlobKey MapBlobKey = new BlobKey( areaDatas.get(i).getPath() );
					ServingUrlOptions MapServOpt = ServingUrlOptions.Builder.withBlobKey(MapBlobKey);
					String MapUrl = imagesService.getServingUrl(MapServOpt) + "=s1600";
					
					
					str = Double.toString(areaDatas.get(i).getX()) + ",";
					str += Double.toString(areaDatas.get(i).getY()) + ",";
					str += Double.toString(areaDatas.get(i).getM()) + ",";
					str += Double.toString(areaDatas.get(i).getOWS()) + ",";
					str += MapUrl;

					return str;
				}
			} else {
				str = "none";
			}
		}

		return str;
	}
}

