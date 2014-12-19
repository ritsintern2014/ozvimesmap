package com.update.ozvimes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.ozvimes.AreaData;
import com.common.ozvimes.Item;
import com.common.ozvimes.PMF;

public class UpdatePosition extends HttpServlet {
	private static final Logger log = Logger.getLogger(UpdatePosition.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Boolean isFail = false;

		//取得した文字が適しているか、ダメならログを出してすべて破棄

		if(! checkGrammar("ItemID", req.getParameter("ItemID"), "^[A-F0-9]{4}$")) isFail = true;
		if(! checkGrammar("APIDs", req.getParameter("APIDs"), "^[A-F0-9]{4}(,[A-F0-9]{4})*$")) isFail = true;
		if(! checkGrammar("RSSIs",req.getParameter("RSSIs"), "^-{0,1}[0-9]{1,3}(,-{0,1}[0-9]{1,3})*,{0,1}$")) isFail = true;
		if(! checkGrammar("x",req.getParameter("x"), "^-{0,1}[0-9]{1,4}(\\.[0-9]){0,1}$")) isFail = true;
		if(! checkGrammar("y",req.getParameter("y"), "^-{0,1}[0-9]{1,4}(\\.[0-9]){0,1}$")) isFail = true;
		if(! checkGrammar("ozv Accuracy",req.getParameter("oAcc"), "^[0-9]{1,2}(\\.[0-9]){0,1}$")) isFail = true;
		if(! checkGrammar("lattitude",req.getParameter("lat"), "^-{0,1}[0-9]{1,3}(\\.[0-9]{1,5}){0,1}$")) isFail = true;
		if(! checkGrammar("longitude",req.getParameter("lng"), "^-{0,1}[0-9]{1,3}(\\.[0-9]{1,5}){0,1}$")) isFail = true;
		if(! checkGrammar("floor",req.getParameter("floor"), "^-{0,1}[0-9]{1,3}(\\.[05]){0,1}$")) isFail = true;
		if(! checkGrammar("Imes Accuracy",req.getParameter("iAcc"), "^[0-9]{2}(\\.[0-9]){0,1}$")) isFail = true;
		if(! checkGrammar("battery",req.getParameter("battery"), "^[0-9](\\.[0-9]{1,2}){0,1}$")) isFail = true;

		if(isFail)	return;

		//取得したパラメータの格納
		String ItemID = req.getParameter("ItemID");
		String APIDs = req.getParameter("APIDs");	//0x0000 ~ 0xFFFF :0xはおそらくつけない
		String RSSIs = req.getParameter("RSSIs");
		Double x = Double.parseDouble( req.getParameter("x") );
		Double y = Double.parseDouble( req.getParameter("y") );
		// OZVの誤差情報　2015年試験場で実装予定
		Double ozvAcc =Double.parseDouble( req.getParameter("oAcc") );
		Double lat = Double.parseDouble( req.getParameter("lat") );
		Double lng = Double.parseDouble( req.getParameter("lng") );
		Double floor = Double.parseDouble( req.getParameter("floor") );
		Double imesAcc =Double.parseDouble( req.getParameter("iAcc") );
		// 2014/12は未使用
		Double battery = Double.parseDouble( req.getParameter("battery") );

		String AreaID = getArea(APIDs, RSSIs);	//エリアを判別

		//現在日時を取得する
		Calendar c = Calendar.getInstance();
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(c.getTime());

		//既に登録されているかどうか
		String q = "select from " + Item.class.getName() + " where ItemID=='"+ ItemID +"'";
		List<Item> itemdatas = (List<Item>) pm.newQuery(q).execute();

		try{
			if(itemdatas.size() <= 0){	//未登録であればインスタンスの生成＆格納
				Item emp = new Item(ItemID, x, y, ozvAcc, lat, lng, floor, imesAcc, AreaID, battery, time);
				emp.setName("none");
				emp.setIconUrl("none");
				emp.setDestination("---");

				emp.setAPIDs(APIDs);
				emp.setRSSIs(RSSIs);

				//あとはIconUrlとnameと納品先をユーザに決めてもらう必要がある

				pm.makePersistent(emp);

			} else{	//登録済ならば上書き
				Item item = itemdatas.get(0);
				item.setX(x);
				item.setY(y);
				item.setOzvAcc(ozvAcc);
				item.setLat(lat);
				item.setLng(lng);
				item.setFloor(floor);
				item.setImesAcc(imesAcc);
				item.setBattery(battery);
				item.setTime(time);
				item.setAPIDs(APIDs);
				item.setRSSIs(RSSIs);

				item.setAreaID(AreaID);
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

		String strongArea = "";

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

			if( map.get( mostArea ) < areaCount ){
				mostArea = areadatas.get(i).getName();
				//System.out.println(mostArea);
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