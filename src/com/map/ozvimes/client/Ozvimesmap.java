package com.map.ozvimes.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.Circle;
import com.google.maps.gwt.client.CircleOptions;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ozvimesmap implements EntryPoint {
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public void onModuleLoad() {
		//Element lab = Document.get().getElementById("keyarea");
		final Button PageBackBtn = new Button("物品一覧ページに戻る");
//		final Button showMapBtn = new Button("Google Map で見る");

		PageBackBtn.addStyleName("btn2");
//		showMapBtn.addStyleName("btn2");

		//クリックで物品リスト
		PageBackBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/listpage.html");
			}
		});

		String key = "";
		while(key.equals("")){
			key = RootPanel.get("keyarea").getElement().getAttribute("key");
		}
		
		greetingService.greetServer(key, new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				//				RootPanel.get("keyarea").getElement().setInnerHTML(result);
				String[] ItemValue = result.split(",", 0);

				RootPanel.get("g_name").getElement().setPropertyString("value", ItemValue[0]);
				RootPanel.get("g_x").getElement().setPropertyString("value", ItemValue[1]);
				RootPanel.get("g_y").getElement().setPropertyString("value", ItemValue[2]);

				RootPanel.get("g_inAcc").getElement().setPropertyString("value", ItemValue[3]);
				
				RootPanel.get("g_bx").getElement().setPropertyString("value", ItemValue[4]);
				RootPanel.get("g_by").getElement().setPropertyString("value", ItemValue[5]);
				
				double outAcc = Double.parseDouble(ItemValue[6]);
				
				RootPanel.get("g_unitM").getElement().setPropertyString("value", ItemValue[7]);
				RootPanel.get("g_OWS").getElement().setPropertyString("value", ItemValue[8]);
				RootPanel.get("inmap").getElement().setPropertyString("src", ItemValue[9]);
				
				double lat = Double.parseDouble(ItemValue[11]);
				double lng = Double.parseDouble(ItemValue[12]);
				
				
				Hidden names_box = new Hidden();
				names_box.setID("names");
				names_box.setValue(ItemValue[13]);
				RootPanel.get().add(names_box);
				
				Hidden xs_box = new Hidden();
				xs_box.setID("xs");
				xs_box.setValue(ItemValue[14]);
				RootPanel.get().add(xs_box);
				
				Hidden ys_box = new Hidden();
				ys_box.setID("ys");
				ys_box.setValue(ItemValue[15]);
				RootPanel.get().add(ys_box);
				
				Hidden oAccs_box = new Hidden();
				oAccs_box.setID("oAccs");
				oAccs_box.setValue(ItemValue[16]);
				RootPanel.get().add(oAccs_box);
				
				Hidden urls_box = new Hidden();
				urls_box.setID("urls");
				urls_box.setValue(ItemValue[17]);
				RootPanel.get().add(urls_box);
				
				
				MyDialogBox(ItemValue[0], ItemValue[10], lat, lng, outAcc);

				//final Panel viewArea = RootPanel.get("viewArea");

//				RootPanel.get("lookGoogleBtn").add(showMapBtn);
				RootPanel.get("backBtn").add(PageBackBtn);
				//RootPanel.get().add(viewArea);
			}

		});
	}

	public void MyDialogBox(String name, String imgUrl, final double lat, final double lng, final double outAcc){
		LatLng center = LatLng.create(lat, lng);

		Panel mapPanel = new VerticalPanel();
		//mapPanel.setSize("640px", "400px");

		String paneWidth = Integer.toString((int)(Window.getClientWidth()*0.9)) + "px";
		String paneHeight = Integer.toString((int)(Window.getClientHeight()*0.85)) + "px";

		mapPanel.setSize(paneWidth, paneHeight);

		RootPanel.get("gMap").add(mapPanel);

		MapOptions options = MapOptions.create();
		options.setZoom(20.0);//18
		options.setCenter(center);
		options.setMapTypeId(MapTypeId.ROADMAP);
		final GoogleMap maps = GoogleMap.create(mapPanel.getElement(), options);

		MarkerOptions mop = MarkerOptions.create();
		//mop.setTitle("物品");
		mop.setPosition(center);

		//Size mkscale = Size.create(50, 50);
		// MarkerImage mi = MarkerImage.create("http://yajidesign.com/i/0213/0213.png");
		// mi.setSize(mkscale);
		// mop.setIcon(mi);
		
		mapPanel.addStyleName("googlemap");

		
		
		//TODO 住所
		final InfoWindowOptions iop = InfoWindowOptions.create();
		iop.setContent("<h2 align=\"center\">"+name+"</h2><img class='gm-win-img' align=\"center\" src=\""+ imgUrl +"\"></img>");
		/*
		GeocoderRequest request = GeocoderRequest.create();
		request.setLocation(center);
		Geocoder geoCoder = Geocoder.create();

		final String tmpadd = "住所不明";
		geoCoder.geocode(request, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				String address = "";
				if (b == GeocoderStatus.OK) {
					GeocoderResult result = a.get(0);
					tmpadd.replace("住所不明", result.getFormattedAddress().replace(",",":").replace(" ","") );

				} else {

				}
			}      
		});
		
		iop.setContent("<h2 align=\"center\">"+name+"</h2><h3>"+ tmpadd +"</h3><img class='gm-win-img' align=\"center\" src=\""+ imgUrl +"\"></img>");		
		
		*/
		

		final InfoWindow iw = InfoWindow.create(iop);
		iw.setPosition(center);
		iw.open(maps);

		////////////////

		Marker marker = Marker.create(mop);
		marker.setMap(maps);
		marker.setTitle(name);

		marker.addClickListener(new com.google.maps.gwt.client.Marker.ClickHandler(){
			@Override
			public void handle(MouseEvent event) {
				iw.open(maps);
			}
		});



		///////////////

		CircleOptions cop = CircleOptions.create();
		cop.setCenter(center);
		cop.setRadius(outAcc);
		cop.setFillColor("#22aaff");
		cop.setStrokeColor("#0055ff");
		Circle cir = Circle.create(cop);

		cir.setMap(maps);
	}

}
