package com.maplist.ozvimes.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
import com.maplist.ozvimes.client.MapListService;
import com.update.ozvimes.UpdatePosition;

public class MapListServiceImpl extends RemoteServiceServlet implements MapListService {
	private static final Logger log = Logger.getLogger(UpdatePosition.class.getName());
	
	@Override
	public String echoBack(String input, double x, double y, int type){

		String str = "";

		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read

		//データベース全部読み込み
		String query = "select from " + AreaData.class.getName() + " ORDER BY name ASC";
		List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();

		//正規表現でマッチ
		Pattern p = Pattern.compile(".*("+input+").*");

		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		for ( int i = 0; i < datas.size(); ++i ) {
			Matcher m = p.matcher(datas.get( i ).getName() );
			if(m.find() == true){
				String InMapkey = Long.toString( datas.get(i).getKey().getId() );

				String url = "./img/noimage.jpg";
				if(! datas.get(i).getPath().equals("none")){
					BlobKey blobKey = new BlobKey( datas.get(i).getPath() );
					ServingUrlOptions ServOpt = ServingUrlOptions.Builder.withBlobKey(blobKey);
					//ServOpt.crop(true);
					//ServOpt.imageSize(size);
					url = imagesService.getServingUrl(ServOpt);
				}				

				str += datas.get(i).getKey().getId() + ",";
				str += datas.get( i ).getName() + ",";
				str += datas.get( i ).getX() + ",";
				str += datas.get( i ).getY() + ",";
				str += datas.get( i ).getM() + ",";
				//str += datas.get( i ).getOWS() + ",";
				str += datas.get( i ).getOWS() + ",";

				str += url + ",";

				//経緯度は物品固有のため、とりあえず今は、エリア内に存在する物品からとってくる
				String itemQuery = "select from " + Item.class.getName() + " where areaID=='" + datas.get(i).getName() + "'";
				List<Item> itemDatas = (List<Item>) pm.newQuery(itemQuery).execute();


				if(itemDatas.size() > 0){
					str += Double.toString(itemDatas.get(0).getLat()) + ",";
					str += Double.toString(itemDatas.get(0).getLng()) + ",";
					str += itemDatas.get( 0 ).getFloor() + ",";
					str += itemDatas.get( 0 ).getImesAcc() + ",";
				}else{
					str += "36.5770" + ",";
					str += "136.6447" + ",";
					str += "1.0" + ",";
					str += "15.0" + ",";
				}
				

				str += datas.get( i ).getApIDs().replace(","," ") + ",";

			}
		}
		pm.close();

		return str;
	}

	public void DeleteMap(String mapname){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read


		//データベース全部読み込み
		String query = "select from " + AreaData.class.getName();
		List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();

		int i = 0;

		for ( i=0; i < datas.size(); i++ ) {
			if( mapname.equals(datas.get(i).getName() )){

				BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
				BlobKey blobKey = new BlobKey( datas.get(i).getPath() );
				blobstoreService.delete(blobKey);

				pm.deletePersistent( pm.getObjectById( AreaData.class, datas.get(i).getKey() ) );
			}
		}
		
		checkArea();
		
		pm.close();
	}


	public String getImesAreas(){
		PersistenceManager pm = PMF.get().getPersistenceManager();

		/*適当に追加部
 リコー、プラスチック、東京タワ、金閣寺


		ImesArea emp1 = new ImesArea( 111, 36.577013, 136.644793, 15, 1);
		ImesArea emp2 = new ImesArea( 222, 36.324986, 136.35885, 10, 2);
		ImesArea emp3 = new ImesArea( 333, 35.65858, 139.745433, 20, 3);
		ImesArea emp4 = new ImesArea( 444, 35.03937, 135.729243, 10, 4);

		try {
			// オブジェクトの格納
			pm.makePersistent(emp1);
			pm.makePersistent(emp2);
			pm.makePersistent(emp3);
			pm.makePersistent(emp4);

		} finally {
			//pm.close();
		}			*/


		//データベース全部読み込み
		String query = "select from " + AreaData.class.getName()  + " ORDER BY name ASC";
		List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();

		String str = "";

		int i = 0;
		for ( i=0; i < datas.size(); i++ ) {

			//経緯度は物品固有のため、とりあえず今は、エリア内に存在する物品からとってくる
			String itemQuery = "select from " + Item.class.getName() + " where areaID=='" + datas.get(i).getName() + "'";
			List<Item> itemDatas = (List<Item>) pm.newQuery(itemQuery).execute();


			str += datas.get( i ).getName() + ",";
			if(itemDatas.size()!=0){
				str += itemDatas.get( 0 ).getLat() + ",";
				str += itemDatas.get( 0 ).getLng() + ",";
				str += itemDatas.get( 0 ).getFloor() + ",";
				str += itemDatas.get( 0 ).getImesAcc() + ",";
			} else{
				str += "36" + ",";
				str += "136" + ",";
				str += "2.0" + ",";
				str += "15.0" + ",";
			}

		}

		pm.close();

		return str;
	}	
	
	public Integer makeArea(String mapname,String areaIDs, String key){
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if(key.equals("")){
			String query = "select from " + AreaData.class.getName();
			List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();
	
			int i = 0;
			for ( i=0; i < datas.size(); i++ ) {
				if(datas.get(i).getName().equals(mapname)){
					return -1;
				}
			}
	
			//			InMap cMap = pm.getObjectById(InMap.class, datas.get(i).getKey().getId());
			AreaData area = new AreaData(mapname, areaIDs);
			area.setPath("none");
	
			//			cMap.setAreaID(Area);
	
	
			try {
				// オブジェクトの格納
				pm.makePersistent(area);
				checkArea();
	
			} finally {
				pm.close();
				return 1;
			}
		}
		
		else{
			String query = "select from " + AreaData.class.getName();
			List<AreaData> datas = (List<AreaData>) pm.newQuery(query).execute();
			
			int i = 0;
			for ( i=0; i < datas.size(); i++ ) {
				if(Long.toString( datas.get(i).getKey().getId() ).equals(key) ){
					datas.get(i).setName(mapname);
					datas.get(i).setApIDs(areaIDs);
					
					checkArea();
					
					return 1;
				}
			}
			
		}
		
		return -2;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void checkArea(){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		
		String query = "select from " + Item.class.getName();
		List<Item> datas = (List<Item>) pm.newQuery(query).execute();
		
		int i = 0;
		
	try{
		
		for(i=0;i<datas.size();i++){
			String AreaID = getArea(datas.get(i).getAPIDs(), datas.get(i).getRSSIs());	//エリアを判別
		
			/*Item emp = new Item(ItemID, x, y, ozvAcc, lat, lng, floor, imesAcc, AreaID, battery, time);
			emp.setName("none");
			emp.setIconUrl("none");*/

			//あとはIconUrlとnameをユーザに決めてもらう必要がある

			

			datas.get(i).setAreaID(AreaID);
		}
	}finally{
		pm.close();
	}

}

private String getArea(String APIDs, String RSSIs){	//どのエリアに物品があるか判別
	List<Integer> hitAreas = new ArrayList<Integer>();
	List<Integer> hitCount = new ArrayList<Integer>();

	String[] ItemApIDs = APIDs.split(",", 0);
	//String[] ItemRssis = APIDs.split(",", 0);

	Map<String, Integer> map = new HashMap<String, Integer>();

	PersistenceManager pm = PMF.get().getPersistenceManager();

	String q = "select from " + AreaData.class.getName();
	List<AreaData> areadatas = (List<AreaData>) pm.newQuery(q).execute();

	if(areadatas.size() <= 0){	//APIDが存在しない場合はemptyと返す
		return "empty";
	}

	String strongArea = "failed";
	
	int i = 0;															//タグが取得したApの数
	for(i=0; i<ItemApIDs.length; i++){
		int j = 0;														//DBに登録されたエリアの数
		for(j=0;j<areadatas.size();j++){
			String[] DbApIDs = areadatas.get(j).getApIDs().split(",", 0);	//エリアの1レコードからAPのID群を取得

			int k = 0;													//エリアごとに登録されたAPの数
			for(k=0; k<DbApIDs.length;k++){


				if(ItemApIDs[i].equals( DbApIDs[k] )){							//取得したAPのID群とItemApIDsを比較
					//hitAreas.add( j );									//一致した時のエリア番号を格納

					String areaName = areadatas.get(j).getName();

					if(map.size()==0)	strongArea = areaName;

					if(map.get(areaName) == null)	map.put(areaName, 1);	//一番数の多かったエリアを現在地とする	数が同じ場合、先に出てきた方優先（RSSIが強い順に出るため）

					else{
						int tmp = map.get( areaName );
						tmp++;
						map.put(areaName, tmp);	//エリアごとのタグ数カウント
					}
				}
			}	
		}
	}

	String mostArea = strongArea;
	for(i=0; i<map.size(); i++){	//各エリアにおいてAPIDが一致した数

		int areaCount = 0;

		if(map.get(areadatas.get(i).getName()) == null){
			areaCount = 0;
		}else{
			areaCount = map.get(areadatas.get(i).getName());
		}

		if( map.get( mostArea ) != null){
			if( map.get( mostArea ) < areaCount ){
				mostArea = areadatas.get(i).getName();
				//System.out.println(mostArea);
			}
		} else{
			mostArea = "failed";
		}
	}

	String result = mostArea;

	//		String result = strongArea;

	//		result = areadatas.get(hitAreas.get(0)).getName();	//先頭が最もRSSI値が大きい


	pm.close();

	return result;	//判別されたエリアの名前を返す
}

private Boolean checkGrammar(String reqName, String str, String pat){	//正規表現と一致するか
	String spMark = "[\\!\"#$%&'\\(\\)=~\\|`\\{\\+\\*\\}<>\\?_\\^\\@\\[;:\\]/]";
	Pattern p1 = Pattern.compile(spMark);
	Matcher m1 = p1.matcher( str );
	
	//特殊文字を含む場合、棄却
	if(m1.find() == true){
		log.warning(reqName + " not match.\nIt has bad character.");
		return false;
	}
	
	//マッチしなかった場合、棄却		
	Pattern p2 = Pattern.compile(pat);
	Matcher m2 = p2.matcher( str );
	
	if(m2.find() == false){
		log.warning(reqName + " not match.");
		return false;
	}

	return true;
}
}