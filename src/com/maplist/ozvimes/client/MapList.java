package com.maplist.ozvimes.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

public class MapList implements EntryPoint {

	private Panel wordpanel = new VerticalPanel();
	private Panel searchpanel = new VerticalPanel();
	private Panel addpanel = new VerticalPanel();

	private TextBox input = new TextBox();
	private Button buttonSend = new Button("検索");
	private Button MapAdd = new Button("");

	private Button itemPage = new Button("物品");
	//private Button MapAdmin = new Button("地図管理");

	final Element tableElement=(Element) Document.get().getElementById("htmltable");

	private final MapListServiceAsync service = GWT.create(MapListService.class);
	@Override
	public void onModuleLoad() {
		
		showLoader();
		
		this.wordpanel.add(this.input);
		this.searchpanel.add(this.buttonSend);
		this.addpanel.add(this.MapAdd);
		//this.panel.add(this.lbResult);
		//Element span = Document.get().createSpanElement();

		buttonSend.addStyleName("btn2");
		MapAdd.addStyleName("btn2");
		
		final MyDialogBox mbox = new MyDialogBox("");

/*		//クリックで地図リスト
		this.MapAdmin.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/MapList.html");
			}
		});
*/
		//クリックで物品リスト
		this.itemPage.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/listpage.html");
			}
		});


		ShowMaps("");

		//textareaで部分一致検索
		this.buttonSend.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				ShowMaps(input.getText());
			}
		});

		this.MapAdd.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				//Window.Location.replace("/mapedit.html");
				mbox.center();
				mbox.show();
			}
		});

		itemPage.addStyleName("btn2");
//		MapAdmin.addStyleName("btn2");

		RootPanel.get("word_area").add(this.wordpanel);
		RootPanel.get("search_button").add(this.searchpanel);
		RootPanel.get("add_area").add(this.addpanel); 

		RootPanel.get("ItemPage").add(this.itemPage);
//		RootPanel.get("MapAdmin").add(this.MapAdmin);


	}



	public void ShowMaps(String word){
		/*全Entity表示*/
		service.echoBack(word, 0, 0, 0, new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				final ArrayList<InMap> Maps = new ArrayList<InMap>();
				String[] MapValue = result.split(",", 0);

				//Window.alert(result);

				final ArrayList<Long> IDs = new ArrayList<Long>();
				ArrayList<String> names = new ArrayList<String>();
				ArrayList<Double> lats = new ArrayList<Double>();
				ArrayList<Double> lngs = new ArrayList<Double>();
				ArrayList<Double> floors = new ArrayList<Double>();
				ArrayList<Double> iAccs = new ArrayList<Double>();

				String str = "<tr class=\"tableindex\">";
				str += "<td class='tbicon'>地図</td>";
				str += "<td>名前</td>";
				//str += "<td>(x ,y)</td>";
				str += "<td>  住所  </td>";
				//			str += "<td>  緯度  </td>";
							str += "<td>  APのID  </td>";
				str += "<td class='regi'>  変更  </td>";
				str += "<td class='del'>  削除  </td>";
				str += "</tr>";

				int i = 0;
				int valLength = 12;
				for(i=0; i < MapValue.length/valLength; i++){
					Maps.add( new InMap( MapValue[0+i*valLength], MapValue[1+i*valLength], Double.valueOf(MapValue[2+i*valLength]), Double.valueOf(MapValue[3+i*valLength]), Double.valueOf(MapValue[4+i*valLength]), Double.valueOf(MapValue[5+i*valLength]), MapValue[6+i*valLength] ) );

					if(i%2==1)
						str += "<tr class=\"tablerecode1\">";
					else{
						str += "<tr class=\"tablerecode2\">";
					}


					//str += "<tr>";
					str += "<td class='tbicon'><a href='/mapedit.html?"+MapValue[0+i*valLength]+ "'><img src='" + Maps.get(i).getPath() + "'></img></a></td>";
					str += "<td><a class='name' href=\"Ozvimesmap.html?"+ Maps.get(i).getKey() + "\">"+Maps.get( i ).getName() +"</a></td>";
					//str += "<td>("+Maps.get( i ).getX()+ ", "+ Maps.get(i).getY()+ ")</td>";
					str += "<td><div id=\"UpdateBtn"+ i +"\" align=\"center\"></div></td>";
					//					str += "<td>"+ MapValue[7+i*valLength] +"</td>";
										str += "<td>"+ MapValue[11+i*valLength] +"</td>";
					str += "<td class='regi'>"+ "<div id=\"editBtn"+ i +"\" class=\"btn1\"></div>" +"</td>";
					str += "<td class='del'><div id=\"deleteBtn"+ i +"\" align=\"center\" class='btn1 del'></div></td>";
					str += "</tr>";

					IDs.add(Long.parseLong(MapValue[0+i*valLength]));

					names.add(Maps.get( i ).getName());
					lats.add(Double.parseDouble(MapValue[7+i*valLength]));
					lngs.add(Double.parseDouble(MapValue[8+i*valLength]));
					floors.add(Double.parseDouble(MapValue[9+i*valLength]));
					iAccs.add(Double.parseDouble(MapValue[10+i*valLength]));
				}

				tableElement.setInnerHTML(str);
				//Window.alert(str);

				for(i=0; i < MapValue.length/valLength; i++){

					Button editBtn = new Button("変更");

					final int ii = i;

					editBtn.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							
							//Window.Location.replace("/mapedit.html?"+IDs.get(ii));
							
							//TODO
							MyDialogBox ubox = new MyDialogBox(Long.toString(IDs.get(ii)));
							ubox.updateBox();
							ubox.show();
							ubox.center();
						}
					});
					editBtn.addStyleName("btn1 upbtn");
					RootPanel.get("editBtn"+i).add(editBtn);

					Panel deletePanel = new VerticalPanel();
					Button deleteButton = new Button("");

					Panel UpdatePanel = new VerticalPanel();
					Button UpdateButton = new Button("");

					deleteButton.addStyleName("btn1 delbtn");
					UpdateButton.addStyleName("btn1 addressbtn");

					final String keyVal = Maps.get(i).getName();//getKey();
					//Window.alert(keyVal);
					deleteButton.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event){
							//Window.alert(keyVal);
							service.DeleteMap(keyVal, new AsyncCallback() {
								@Override
								public void onFailure(Throwable caught) {
									//Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(Object result) {
									ShowMaps("");
								}
							});

						}
					});

					final String a = Maps.get(i).getKey();
					final UpdateDialogBox upbox = new UpdateDialogBox(a, names.get(i), lats.get(i), lngs.get(i), floors.get(i), iAccs.get(i));

					UpdateButton.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event){
							//RootPanel.get().remove(upbox);
							upbox.show();
							upbox.setGmap();
							//upbox.center();
						}
					});

					String delname = "deleteBtn" + i;
					String upname = "UpdateBtn" + i;

					deletePanel.add(deleteButton);
					UpdatePanel.add(UpdateButton);
					RootPanel.get(delname).add(deletePanel);
					RootPanel.get(upname).add(UpdatePanel);

					//upbox.hide();
					
					readCB();
					
					hideLoader();
				}
			}
		});
	}
	
	private native static void readCB() /*-{
		$wnd.disp();
	}-*/;
	
	private native static void hideLoader() /*-{
		$wnd.hideLoader();
	}-*/;

	private native static void showLoader() /*-{
		$wnd.showLoader();
	}-*/;

	private class UpdateDialogBox extends DialogBox {
		private GoogleMap maps;
		private VerticalPanel panel = new VerticalPanel();
		private Button closeButton = new Button("Cancel");
		//		private Button sendButton = new Button("Send");
		//		private ArrayList<ImesArea> ImesAreas = new ArrayList<ImesArea>();
		private boolean isFirst = true;
		private String name = "none";
		private double lat = 0.0;
		private double lng = 0.0;
		private double floor = 0.0;
		private double iAcc = 0.0;
		private LatLng center = LatLng.create(36,136);

		public UpdateDialogBox(final String mapkey, String name, double lat, double lng, double floor, double iAcc){
			this.name = name;
			this.lat = lat;
			this.lng = lng;

			this.center = LatLng.create(lat, lng);

			this.floor = floor;
			this.iAcc = iAcc;

			//Window.alert(name+"="+Double.toString(lat) + ":"+ Double.toString(lng));

			show();
			/*
			String dWidth = Integer.toString((int)(Window.getClientWidth()*0.9)) + "px";
			String dHeight = Integer.toString((int)(Window.getClientHeight()*0.9)) + "px";
			setSize(dWidth,dHeight);*/

			setText(name);


			closeButton.addStyleName("dlogbtn1");
			//sendButton.addStyleName("dlogbtn1");

			closeButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});



			//HTML sendMes = new HTML();
			//sendMes.setHTML("<h1>送る値</h1>");

			//final ListBox lb = new ListBox();
			/*
			service.getImesAreas(new AsyncCallback<String>(){
				@Override
				public void onFailure(Throwable caught) {
					//					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(String result) {
					String[] AreaIDs = result.split(",", 0);

					int i = 0;
					for(i=0; i < AreaIDs.length/5; i++){
						//ImesAreas.add( new ImesArea( AreaIDs[0+i*5], Double.parseDouble(AreaIDs[1+i*5]), Double.parseDouble(AreaIDs[2+i*5]), Double.parseDouble(AreaIDs[3+i*5]),  Double.parseDouble(AreaIDs[4+i*5]) ) );
		//				lb.addItem(ImesAreas.get(i).getAreaID());
					}

			//		lb.setVisibleItemCount(0);
				}
			});*/
			/*
			sendButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event){
					service.setArea( mapkey,Integer.parseInt(lb.getValue( lb.getSelectedIndex() )), new AsyncCallback(){
						@Override
						public void onFailure(Throwable caught) {
							//						Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Object result) {
							//Window.alert(result);
							hide();
							ShowMaps("");
						}
					});
				}

			});

			lb.addStyleName("gwt-Textbox");
			lb.addStyleName("map-dlog-lb");
			//lb.width();

			// Add it to the root panel.
			panel.add(lb);
			 */
			//Size mkscale = Size.create(50, 50);
			// MarkerImage mi = MarkerImage.create("http://yajidesign.com/i/0213/0213.png");
			// mi.setSize(mkscale);
			// mop.setIcon(mi);

			////////////////

			/*
			lb.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					LatLng nCenter = LatLng.create( ImesAreas.get(lb.getSelectedIndex()).getLat(), ImesAreas.get(lb.getSelectedIndex()).getLng() );
					maps.setCenter(nCenter);
					maps.setZoom(20.0);

					//marker.setPosition(nCenter);
				}
			});
			 */
			////////////////





			setWidget(panel);

			hide();
		}

		private void setGmap(){
			if(isFirst){
				center();
				setPopupPosition((int)(Window.getClientWidth()*0.2), (int)(Window.getClientHeight()*0.1));
				Panel mapPanel = new VerticalPanel();

				String paneWidth = Integer.toString((int)(Window.getClientWidth()*0.6)) + "px";
				String paneHeight = Integer.toString((int)(Window.getClientHeight()*0.6)) + "px";

				//				LatLng center = LatLng.create(lat, lng);

				mapPanel.addStyleName("googlemap");
				//panel.setSize(paneWidth, paneHeight);
				mapPanel.setSize(paneWidth, paneHeight);

				panel.add(mapPanel);

				MapOptions options = MapOptions.create();
				options.setZoom(20.0);//18
				options.setCenter(center);

				options.setMapTypeId(MapTypeId.ROADMAP);

				//final GoogleMap
				maps = GoogleMap.create(mapPanel.getElement(), options);

				//				Window.alert(Integer.toString(ImesAreas.size()));

				//				LatLng makerCenter = LatLng.create(lat, lng);
				setMarker();

				//						setMarker(makerCenter, ImesAreas.get(i).getFloor());


				//options.setCenter(center);
				isFirst=false;
			} else{
				center();
			}

			//panel.add(sendButton);
			panel.add(closeButton);

			//			center();
		}

		private void setMarker(){
			MarkerOptions mop = MarkerOptions.create();
			mop.setPosition(center);
			final Marker marker = Marker.create(mop);
			marker.setMap(maps);

			final String stFloor = Double.toString(floor);			

			final InfoWindowOptions iop = InfoWindowOptions.create();
			//iop.setContent("<h2 align=\"center\"><nobr>"+ name +" F</nobr></h2>");

			GeocoderRequest request = GeocoderRequest.create();
			request.setLocation(marker.getPosition());
			Geocoder geoCoder = Geocoder.create();

			// tmpname = "";
			geoCoder.geocode(request, new Geocoder.Callback() {
				@Override
				public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
					if (b == GeocoderStatus.OK) {
						GeocoderResult result = a.get(0);
						String address = result.getFormattedAddress().replace(",",":").replace(" ","");

						//  tmpname = address;
						//iop.setContent("aa");

						//iop.setContent(tmpname);
						iop.setContent("<h2 align=\"center\"><nobr>"+ address +"</nobr><br><nobr>"+ stFloor +" 階</nobr></h2>");
						final InfoWindow iw = InfoWindow.create(iop);
						iw.setPosition(center);
						iw.open(maps);

						//Window.alert(tmpname);

						marker.addClickListener(new com.google.maps.gwt.client.Marker.ClickHandler(){
							@Override
							public void handle(MouseEvent event) {
								iw.open(maps);
							}
						});

						LatLng firstCenter = LatLng.create(lat, lng);
						maps.setCenter(firstCenter);

					} else {
						//iop.setContent("<h2 align=\"center\"><nobr>"+ "-住所取得不可-" +"</nobr><nobr>"+ name +" 階</nobr></h2>");
						//        	iop.setContent("失敗");

						iop.setContent("住所不明");
						final InfoWindow iw = InfoWindow.create(iop);
						iw.setPosition(center);
						iw.open(maps);

						//Window.alert(tmpname);

						marker.addClickListener(new com.google.maps.gwt.client.Marker.ClickHandler(){
							@Override
							public void handle(MouseEvent event) {
								iw.open(maps);
							}
						});

						LatLng firstCenter = LatLng.create(lat, lng);
						maps.setCenter(firstCenter);
					}

					//Window.alert(""+floor);
				}      
			});
		}
	}


	private class MyDialogBox extends DialogBox {
		String key = "";
		Button closeButton = new Button("Cancel");
		Button sendButton = new Button("Send");
		Button editButton = new Button("地図を編集");
		VerticalPanel panel = new VerticalPanel();
		
		public MyDialogBox(final String key){
			this.key = key;
			
			setText("エリアの追加");


			closeButton.addStyleName("dlogbtn1");
			sendButton.addStyleName("dlogbtn1");

			closeButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
			
			Label nameLabel = new Label("エリアの名前　例: ○○工場 △F ××室");
			Label idLabel = new Label("エリアのAPのアドレス　例: ABCD,12EF,5302");

			final TextBox nameBox = new TextBox();
			final TextBox apIdBox = new TextBox();

			sendButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					
					showLoader();
					
					//Pattern p = Pattern.compile("^[A-F0-9]{4}(,[A-F0-9]{4})*,{0,1}$");	//ABCD,2213,D232	とか
					//Matcher m = p.matcher(apIdBox.getValue());
					
					String a = "[!\"#$%&'\\(\\)=~\\|`\\{\\+\\*\\}<>\\?_\\-\\^\\@\\[;:\\]\\,\\./]";
					Boolean isMatc = matches(a, nameBox.getValue());
					Boolean isMatch = matches("^[A-F0-9]{4}(,[A-F0-9]{4})*,{0,1}$", apIdBox.getValue().toUpperCase());
					
					if(isMatc == true){
						Window.alert("! \" # $ % & ' ( ) = ~ | ` { + * } < > ? _ - ^ @ [ ; : ] , .  /\nこれらの記号は使用しないでください。");
						hideLoader();
						return;
					}
					
					if(isMatch == false){
						Window.alert("アクセスポイントのアドレスは\n半角数字とアルファベット、合わせて４ケタを「,」で区切って入力してください\n例：　ABCD,221F,3391");
						hideLoader();
						return;
					}
					
					service.makeArea(nameBox.getValue(), apIdBox.getValue().toUpperCase(), key, new AsyncCallback<Integer>(){
						@Override
						public void onFailure(Throwable caught) {
							//						Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Integer result) {
							//Window.alert(result);
							if(result == -1){
								Window.alert("そのエリア名は既に登録されています。\n名前を変更してください。");
								hideLoader();
							}
							else if(result == -2){
								Window.alert("エラー：Keyが一致しませんでした。\nページを読み込み直してください。");
								hideLoader();
							}
							else{
								hide();
								ShowMaps("");
							}
						}
					});
				}

			});
			
			panel.add(nameLabel);
			panel.add(nameBox);	
			panel.add(idLabel);
			panel.add(apIdBox);	
			panel.add(sendButton);
			panel.add(closeButton);
			setWidget(panel);
			
		}
		/*
		public void createDBox(){
			panel.remove(editButton);
		}*/

		public void updateBox(){
			setText("エリアの編集");
			editButton.addStyleName("dlogbtn1");
			editButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					Window.Location.replace("/mapedit.html?" + key);	
				}
				
			});
			
			panel.add(editButton);
			panel.add(sendButton);
			panel.add(closeButton);
			setWidget(panel);
		}
	}
	private native static boolean matches(String strRegExp, String value) /*-{	
	var pattern = new RegExp(strRegExp);
	return value.search(pattern) != -1;
	}-*/;

	public class InMap {
		private String key;

		private String name;

		private double BPx;	//map上の基準点の位置

		private double BPy;

		private double unitM;	//１メートル

		private double OriginWindowSize;	//設定時のウィンドウサイズ

		private String path;	//アドレス

		public InMap(String key, String name, double BPx, double BPy, double meter, double OWS, String path) {
			this.key = key;
			this.name = name;
			this.BPx = BPx;
			this.BPy = BPy;
			this.unitM = meter;
			this.OriginWindowSize = OWS;
			this.path = path;
		}

		public String getKey() {
			return this.key;
		}

		public String getName() {
			return this.name;
		}

		public double getX() {
			return this.BPx;
		}

		public double getY() {
			return this.BPy;
		}

		public double getM() {
			return this.unitM;
		}

		public double getOWS() {
			return this.OriginWindowSize;
		}

		public String getPath() {
			return this.path;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setX(double BPx) {
			this.BPx = BPx;
		}

		public void setY(double BPy) {
			this.BPy = BPy;
		}

		public void setM(double M) {
			this.unitM = M;
		}

		public void setOWS(double OWS) {
			this.OriginWindowSize = OWS;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}

	public class ImesArea {
		private String key;

		private String AreaID;	//AreaのID

		private double latitude;	//緯度

		private double longitude;	//経度

		private double accuracy;	//IMESの誤差

		private double floor;	//IMESの階

		public ImesArea( String areaID, double lat, double lng, double acc, double floor) {
			this.AreaID = areaID;
			this.latitude = lat;
			this.longitude = lng;
			this.accuracy = acc;
			this.floor = floor;
		}

		public String getKey() {
			return this.key;
		}

		public String getAreaID() {
			return this.AreaID;
		}

		public double getLat() {
			return this.latitude;
		}

		public double getLng() {
			return this.longitude;
		}

		public double getFloor() {
			return this.floor;
		}

		public double getAcc() {
			return this.accuracy;
		}

		public void setAreaID(String areaid) {
			this.AreaID = areaid;
		}

		public void setLat(double lat) {
			this.latitude = lat;
		}

		public void setLng(double lng) {
			this.longitude = lng;
		}

		public void setFloor(double floor) {
			this.floor = floor;
		}

		public void setAcc(double acc) {
			this.accuracy = acc;
		}

	}

}
