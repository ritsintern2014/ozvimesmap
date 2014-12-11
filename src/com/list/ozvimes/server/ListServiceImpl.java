package com.list.ozvimes.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;

import com.common.ozvimes.AreaData;
import com.common.ozvimes.Item;
import com.common.ozvimes.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.list.ozvimes.client.ListService;

public class ListServiceImpl extends RemoteServiceServlet implements ListService {
	@Override
	public String echoBack(String input, double x, double y, String MapName, int column, int type){

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String str = "";
/*
		AreaData areaA = new AreaData("area1","1111,2222,3333,4444");
		AreaData areaB = new AreaData("area2","5555,6666,7777");
		AreaData areaC = new AreaData("area3","ABCD,EF12,3456,8888");
		AreaData areaD = new AreaData("area4","AAAA,BBBB,CCCC");
		AreaData areaE = new AreaData("area5","DDDD,EEEE,FFFF");

		areaA.setPath("none");
		areaB.setPath("none");
		areaC.setPath("none");
		areaD.setPath("none");
		areaE.setPath("none");

		pm.makePersistent(areaA);
		pm.makePersistent(areaB);
		pm.makePersistent(areaC);
		pm.makePersistent(areaD);
		pm.makePersistent(areaE);

*/		
		if(type == 0){
			ImagesService imagesService = ImagesServiceFactory.getImagesService();

			//read

			//データベース全部読み込み
			String query = "select from " + Item.class.getName() + " ORDER BY LastTime DESC";
			List<Item> datas = (List<Item>) pm.newQuery(query).execute();

			if(datas.size() <= 0) return "empty";

			//正規表現でマッチ
			Pattern p = Pattern.compile(".*("+input+").*");

			//String str = "";	//String.valueOf(datas.size());

			for ( int i = 0; i < datas.size(); ++i ) {
				String dataStr = "";
				
				if(column == 0)	dataStr = datas.get( i ).getName();
				else if(column == 1)	dataStr = datas.get( i ).getAreaID();
				else if(column == 2)	dataStr = datas.get( i ).getDestination();
				
				
				Matcher m = p.matcher( dataStr );
				
				
				
				
				if(m.find() == true){
					String itemkey = Long.toString( datas.get(i).getKey().getId() );

					String url = "./img/noimage.jpg";
					if(! datas.get(i).getIconUrl().equals("none") ){
						BlobKey blobKey = new BlobKey( datas.get(i).getIconUrl() );
						ServingUrlOptions ServOpt = ServingUrlOptions.Builder.withBlobKey(blobKey);
						//ServOpt.crop(true);
						//ServOpt.imageSize(size);
						url = imagesService.getServingUrl(ServOpt)+"=s250";
					}

					String areaId = datas.get(i).getAreaID();

					//					String aq = "select from " + AreaData.class.getName() + " where name=='"+ areaId +"'";
					//					List<AreaData> areaDatas = (List<AreaData>) pm.newQuery(aq).execute();

					str += itemkey + ",";
					str += datas.get( i ).getName() + ",";
					str += datas.get( i ).getX() + ",";
					str += datas.get( i ).getY() + ",";
					str += datas.get( i ).getOzvAcc() + ",";
					str += datas.get( i ).getAreaID() + ",";
					str += url+",";
					
					str += datas.get( i ).getDestination() + ",";
					str += datas.get( i ).getBattery() + ",";

					str += datas.get( i ).getItemID() + ",";
				}
			}
		}


		if(type==1){	//新しく作る

			String mq = "select from " + AreaData.class.getName() + " where name=='"+ MapName +"'";
			List<AreaData> mapdatas = (List<AreaData>) pm.newQuery(mq).execute();

			//現在日時を取得する
			Calendar c = Calendar.getInstance();
			TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
			// TimeZone tokyo = TimeZone.getTimeZone("JST");
			// c.setTimeZone(tokyo);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdf.format(c.getTime());


			double oAcc = 3.0;
			double lat = 136.0;
			double lng = 36.0;
			double floor = 3.0;
			double iAcc = 15.0;

			String areaId = "area1";

			double battery = 1.5;

			//			Item emp = new Item( input, x, y, 2, MapName, time);// mapdatas.get(0).getPath() );
			Item emp = new Item(input, x, y, oAcc, lat, lng, floor, iAcc, areaId, battery, time );// mapdatas.get(0).getPath() );
			emp.setIconUrl("none");
			emp.setName(input);

			try {
				// オブジェクトの格納
				pm.makePersistent(emp);

			} finally {
			}

			final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			final String uploadUrl = blobstoreService.createUploadUrl("/upload");

			str = emp.getKey().getId() + "," +  uploadUrl;

		}

		pm.close();

		return str;
	}

	public ArrayList<String> getMapList(){

		ArrayList<String> str = new ArrayList<String>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read

		//データベース全部読み込み
		String query = "select from " + AreaData.class.getName() +" ORDER BY name DESC";
		List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();

		int i = 0;

		for ( i=0; i < datas.size(); i++ ) {
			str.add( datas.get(i).getName() );
		}
		pm.close();

		return str;
	}

	public void DeleteItem(String itemkey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read

		//データベース全部読み込み
		String query = "select from " + Item.class.getName();
		List<Item> datas = (List<Item>) pm.newQuery(query).execute();

		int i = 0;

		for ( i=0; i < datas.size(); i++ ) {
			if( datas.get(i).getKey().getId() == Long.valueOf(itemkey) ){

				BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
				BlobKey blobKey = new BlobKey( datas.get(i).getIconUrl() );
				blobstoreService.delete(blobKey);

				pm.deletePersistent( pm.getObjectById( Item.class, datas.get(i).getKey() ) );
			}
		}
		pm.close();

	}

	public String UpdateItem(String itemkey, String name, double x, double y,double Acc, String MapName, String dis){

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + Item.class.getName();
		List<Item> datas = (List<Item>) pm.newQuery(query).execute();

		int i = 0;

		for ( i=0; i < datas.size(); i++ ) {
			if( datas.get(i).getKey().getId() == Long.valueOf(itemkey) ){

				Item cItem = pm.getObjectById(Item.class, datas.get(i).getKey().getId());
				
				cItem.setName(name);
				cItem.setX(x);
				cItem.setY(y);
				cItem.setOzvAcc(Acc);
				cItem.setAreaID(MapName);
				cItem.setDestination(dis);

				//現在日時を取得する
				Calendar c = Calendar.getInstance();
				TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
				//TimeZone tokyo = TimeZone.getTimeZone("JST");
				//c.setTimeZone(tokyo);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String time = sdf.format(c.getTime());

				cItem.setTime(time);
				/*
				try {
					// オブジェクトの格納
					pm.makePersistent(cItem);

				} finally {*/
				pm.close();
				//}
			}

		}
		
		final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		final String uploadUrl = blobstoreService.createUploadUrl("/upload");

		return uploadUrl;
	}
}