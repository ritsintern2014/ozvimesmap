package com.map.ozvimes.server;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.common.ozvimes.AreaData;
import com.common.ozvimes.Item;
import com.common.ozvimes.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.map.ozvimes.client.GreetingService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	//private static final Logger log = Logger.getLogger(UpdatePosition.class.getName());

	public String greetServer(String input) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//read

		//データベース全部読み込み
		String query = "select from " + Item.class.getName();
		List<Item> datas = (List<Item>) pm.newQuery(query).execute();
		
		String str = "";	//String.valueOf(datas.size());
		
		//log.warning("Item");
		
		for ( int i = 0; i < datas.size(); ++i ) {
			if(datas.get(i).getKey().getId() == Long.parseLong(input)){
				String itemkey = Long.toString( datas.get(i).getKey().getId() );
				
				String mq = "select from " + AreaData.class.getName() + " where name=='"+ datas.get(i).getAreaID() +"'";
				List<AreaData> areaDatas = (List<AreaData>) pm.newQuery(mq).execute();
				
				AreaData Areadata= areaDatas.get(0);
				/*
				String areaQuery = "select from " + ImesArea.class.getName() + " where AreaID=="+ (int)ItemMap.getAreaID();
				List<ImesArea> areaDatas = (List<ImesArea>) pm.newQuery(areaQuery).execute();
				*/
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				
				
				String MapUrl = "";
				if(Areadata.getPath().equals("none"))	MapUrl = "./img/noimage.jpg";
				else {
					BlobKey MapBlobKey = new BlobKey( Areadata.getPath() );
					ServingUrlOptions MapServOpt = ServingUrlOptions.Builder.withBlobKey(MapBlobKey);
					MapUrl = imagesService.getServingUrl(MapServOpt) + "=s1600";
				}
				
				String IconUrl= "";
				if(datas.get(i).getIconUrl().equals("none"))	IconUrl = "./img/noimage.jpg";
				else {
					BlobKey IconBlobKey = new BlobKey( datas.get(i).getIconUrl() );
					ServingUrlOptions IconServOpt = ServingUrlOptions.Builder.withBlobKey(IconBlobKey);
					IconUrl = imagesService.getServingUrl(IconServOpt) + "=s1600";
				}

				
				//str += itemkey + "";
				str = datas.get(i).getName() +",";
				str += datas.get(i).getX() +",";
				str += datas.get(i).getY() +",";
				str += datas.get(i).getOzvAcc() +",";
				str += Areadata.getX() +",";
				str += Areadata.getY()+",";
				str += datas.get(i).getImesAcc() +",";		//MapAcc
				str += Areadata.getM() +",";
				str += Areadata.getOWS() +",";
				str += MapUrl+",";
				str += IconUrl+",";
				
				str += Double.toString(datas.get(i).getLat())+",";
				str += Double.toString(datas.get(i).getLng())+",";
				
				
				//同エリア内の物品取得
				String que = "select from " + Item.class.getName() + " where areaID=='"+ datas.get(i).getAreaID() +"'";
				List<Item> itemDatas = (List<Item>) pm.newQuery(que).execute();
				
				String names = "";
				String xs = "";
				String ys = "";
				String oAccs = "";
				String urls = "";
				/*
				for(i=0;i<itemDatas.size();i++){
					names += itemDatas.get(i).getName() + "@@@";
					xs += itemDatas.get(i).getX()  + "@@@";
					ys += itemDatas.get(i).getY()  + "@@@";
					oAccs += itemDatas.get(i).getOzvAcc()  + "@@@";
					urls += itemDatas.get(i).getIconUrl()  + "@@@";
				}*/
				
				names = datas.get(i).getName();
				xs = Double.toString( datas.get(i).getX() );
				ys = Double.toString( datas.get(i).getY() );
				oAccs = Double.toString( datas.get(i).getOzvAcc() );
				urls = IconUrl;
				
				str += names + ",";
				str += xs + ",";
				str += ys + ",";
				str += oAccs + ",";
				str += urls + ",";
				//str+="<div style=\"font-size:150%;\">" + i + "</div><div class=\"itemname\" style=\"font-size:200%;\"><a href=\"Ozvimesmap.html?"+ itemkey + "\">"+datas.get( i ).getName() +"</a></div><div style=\"font-size:200%;\">("+datas.get( i ).getX()+", "+datas.get( i ).getY()+ ")</div>";
				

				pm.close();
				//log.warning(str);
				return str;
			}
		}
		
		//log.warning("Map");
		
		//入力されたのが、マップのIDだった場合、エリア内のすべての物品を表示
		String mq = "select from " + AreaData.class.getName();
		List<AreaData> areaDatas = (List<AreaData>) pm.newQuery(mq).execute();
		
		for ( int i = 0; i < areaDatas.size(); ++i ) {
			if(Long.toString( areaDatas.get(i).getKey().getId() ).equals(input)){
				AreaData Areadata = areaDatas.get(i);
				
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				
				//log.warning("Hit");
				
				String MapUrl = "";
				if(Areadata.getPath().equals("none"))	MapUrl = "./img/noimage.jpg";
				else {
					BlobKey MapBlobKey = new BlobKey( Areadata.getPath() );
					ServingUrlOptions MapServOpt = ServingUrlOptions.Builder.withBlobKey(MapBlobKey);
					MapUrl = imagesService.getServingUrl(MapServOpt) + "=s1600";
				}
				
				//同エリア内の物品取得
				String que = "select from " + Item.class.getName() + " where areaID=='"+ Areadata.getName() +"'";
				List<Item> itemDatas = (List<Item>) pm.newQuery(que).execute();
				
				String IconUrl= "";
				if(itemDatas.get(0).getIconUrl().equals("none"))	IconUrl = "./img/noimage.jpg";
				else {
					BlobKey IconBlobKey = new BlobKey( itemDatas.get(0).getIconUrl() );
					ServingUrlOptions IconServOpt = ServingUrlOptions.Builder.withBlobKey(IconBlobKey);
					IconUrl = imagesService.getServingUrl(IconServOpt) + "=s1600";
				}
				
				//str += itemkey + "";
				str = itemDatas.get(0).getName() +",";
				str += "showAll" +",";
				str += "showAll" +",";
				str += itemDatas.get(0).getOzvAcc() +",";
				str += Areadata.getX() +",";
				str += Areadata.getY()+",";
				str += itemDatas.get(0).getImesAcc() +",";		//MapAcc
				str += Areadata.getM() +",";
				str += Areadata.getOWS() +",";
				str += MapUrl+",";
				str += IconUrl+",";
				
				str += Double.toString(itemDatas.get(0).getLat())+",";
				str += Double.toString(itemDatas.get(0).getLng())+",";
				
				

				
				String names = "";
				String xs = "";
				String ys = "";
				String oAccs = "";
				String urls = "";
				
				int j = 0;
				for(j=0;j<itemDatas.size();j++){
					names += itemDatas.get(j).getName() + "@@@";
					xs += itemDatas.get(j).getX()  + "@@@";
					ys += itemDatas.get(j).getY()  + "@@@";
					oAccs += itemDatas.get(j).getOzvAcc()  + "@@@";
					
					String iUrl= "";
					if(itemDatas.get(j).getIconUrl().equals("none"))	iUrl = "./img/noimage.jpg";
					else {
						BlobKey IconBlobKey = new BlobKey( itemDatas.get(j).getIconUrl() );
						ServingUrlOptions IconServOpt = ServingUrlOptions.Builder.withBlobKey(IconBlobKey);
						iUrl = imagesService.getServingUrl(IconServOpt) + "=s1600";
					}
					
					
					urls += iUrl + "@@@";
				}
				
				str += names + ",";
				str += xs + ",";
				str += ys + ",";
				str += oAccs + ",";
				str += urls + ",";
				

				pm.close();
				
				//log.warning(str);
				
				return str;
			}
		}
		

		/*
		//正規表現でマッチ

		//Pattern p = Pattern.compile(".*("+input+").*");
		Pattern p = Pattern.compile(".*(p).*");

		String str = "";	//String.valueOf(datas.size());

		for ( int i = 0; i < datas.size(); ++i ) {
			Matcher m = p.matcher(datas.get( i ).getName() );
			if(m.find() == true){
				String itemkey = Long.toString( datas.get(i).getKey().getId() );
				str += itemkey+" : ";
				//str+="<tr><td><div style=\"font-size:150%;\">" + i + "</div></td><td><div class=\"itemname\" style=\"font-size:200%;\"><a href=\"Ozvimesmap.html?"+ itemkey + "\">"+datas.get( i ).getName() +"</a></div></td><td><div style=\"font-size:200%;\">("+datas.get( i ).getX()+", "+datas.get( i ).getY()+ ")</div></td></tr>";
			}
		}*/
		pm.close();
		
		str = "not found : " + input;
		//log.warning(str);
		return str;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 *
	 * @param html the html string to escape
	 * @return the escaped string
	 *//*
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;");
	}*/
}
