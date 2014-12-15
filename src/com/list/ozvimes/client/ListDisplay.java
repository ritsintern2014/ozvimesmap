package com.list.ozvimes.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ListDisplay implements EntryPoint {

	private Panel wordpanel = new VerticalPanel();
	private Panel searchpanel = new VerticalPanel();
	private Panel addpanel = new VerticalPanel();

	private TextBox input = new TextBox();
	private Button buttonSend = new Button("検索");
	private Button itemAdd = new Button();
	
	//private Button itemPage = new Button("物品管理");
	private Button MapAdmin = new Button("地図");

	final Element tableElement=(Element) Document.get().getElementById("htmltable");
	final String str = "";

	CellTable<Item> table = new CellTable<Item>();

	final List<Item> Items = new ArrayList<Item>();

	private final ListServiceAsync service = GWT.create(ListService.class);
	
	@Override
	public void onModuleLoad() {
		
		showLoader();
		
		// TODO Auto-generated method stub
		this.wordpanel.add(this.input);
		this.searchpanel.add(this.buttonSend);
		this.addpanel.add(this.itemAdd);
		//this.panel.add(this.lbResult);
		//Element span = Document.get().createSpanElement();

		//クリックで地図リスト
		this.MapAdmin.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/MapList.html");
			}
		});

/*		//クリックで物品リスト
		this.itemPage.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Locatio
	});
*/
		
		//blastName();
		ShowItems("",0);
		
		
		final ListBox lb = new ListBox();
		lb.addItem("物品名");
		lb.addItem("所在地");
		lb.addItem("納品先");
		lb.setVisibleItemCount(1);
		
		RootPanel.get("list_area").add(lb);

		//textareaで部分一致検索
		this.buttonSend.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				showLoader();
				ShowItems(input.getText(),0);
			}
		});
		

		final MyDialogBox diag = new MyDialogBox();

		this.itemAdd.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				diag.show();
				diag.center();
			}
		});
		
		
//		itemPage.addStyleName("btn2");
		MapAdmin.addStyleName("btn2");
		
		buttonSend.addStyleName("btn2");
		itemAdd.addStyleName("btn2");

		RootPanel.get("word_area").add(this.wordpanel);
		RootPanel.get("search_button").add(this.searchpanel);
		RootPanel.get("add_item").add(this.addpanel); 

//		RootPanel.get("ItemPage").add(this.itemPage);
		RootPanel.get("MapAdmin").add(this.MapAdmin);
		
		/*
		Panel header = RootPanel.get("header");
		Panel main = RootPanel.get("main");
		Panel footer = RootPanel.get("footer");*/
	}


	private class MyDialogBox extends DialogBox {
		public MyDialogBox(){
			setText("物品の追加");
			Button closeButton = new Button("Cancel");
			Button sendButton = new Button("Send");
			
			closeButton.addStyleName("dlogbtn1");
			sendButton.addStyleName("dlogbtn1");

			closeButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});

			VerticalPanel panel = new VerticalPanel();

			//HTML sendMes = new HTML();
			//sendMes.setHTML("<h1>送る値</h1>");

			final TextBox nameBox = new TextBox();
			final TextBox xPosBox = new TextBox();
			final TextBox yPosBox = new TextBox();

			nameBox.setText("物品名称");
			xPosBox.setText("x座標");
			yPosBox.setText("y座標");

			panel.add(nameBox);
			panel.add(xPosBox);
			panel.add(yPosBox);

			final ListBox lb = new ListBox();

			//	String[] mapList;

			service.getMapList(new AsyncCallback<ArrayList<String>>(){
				@Override
				public void onFailure(Throwable caught) {
					//Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					int i = 0;

					for(i=0; i<result.size(); i++){
						lb.addItem(result.get(i));
					}

					lb.setVisibleItemCount(1);
				}
			});

			// Add it to the root panel.
			panel.add(lb);
			lb.addStyleName("gwt-TextBox");

			final FormPanel form = new FormPanel();

			form.setEncoding(FormPanel.ENCODING_MULTIPART);
			form.setMethod(FormPanel.METHOD_POST);

			// Create a panel to hold all of the form widgets.
			VerticalPanel fPane = new VerticalPanel();
			form.setWidget(fPane);

			final Hidden key_box = new Hidden();
			key_box.setName("itemkey");
			fPane.add(key_box);

			Hidden type_box = new Hidden();
			type_box.setName("type");
			type_box.setValue("icon");
			fPane.add(type_box);

			FileUpload upload = new FileUpload();
			upload.setName("icon");
			fPane.add(upload);

			panel.add(form);

			panel.add(sendButton);


			sendButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event){
					service.echoBack(nameBox.getText(), Double.valueOf(xPosBox.getText()), Double.valueOf(yPosBox.getText()), lb.getValue( lb.getSelectedIndex() ), 0, 1, new AsyncCallback<String>(){
						@Override
						public void onFailure(Throwable caught) {
							//Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							//Window.alert(result);
							String[] ItemInfo = result.split(",", 0);

							key_box.setValue(ItemInfo[0]);

							form.setAction(ItemInfo[1]);
							form.submit();

							//ここでhide();を入れると送信前に終了する
						}
					});
				}

			});

			form.addSubmitHandler(new FormPanel.SubmitHandler() {
				public void onSubmit(SubmitEvent event) {
					hide();
					//Window.alert("Start Sending");
					//Window.alert(event.toString());
				}
			});
			form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
				public void onSubmitComplete(SubmitCompleteEvent event) {
					hide();
					Window.alert("Success"+event.getResults());

					ShowItems("",1);
				}
			});
			/*
			HTML qrCode = new HTML("<input type=\"file\" accept=\"image/*;capture=camera\">");

			panel.add(qrCode);*/
			panel.add(closeButton);
			setWidget(panel);
		}
	}

	//JSNI test
	private native void blastName() /*-{
		alert("Hello world");
	}-*/;

	/*
	private void showInfo() {
		  //show(list.find("Intro"), false);
		  blastName(); // added JSNI method
	}
	 */
	public void ShowItems(String word, int lbIndex){		//lbIndex 1:物品名称 2:エリア名 3:納品先
		/* Entity表示 */
		service.echoBack(word, 0, 0, "", lbIndex, 0, new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				//lbResult.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				String[] ItemValue = result.split(",", 0);
				Items.clear();

				//Window.alert(result);

				String str = "<thead><tr class=\"tableindex\">";
				str += "<td class='debug'>ID</td>";
				str += "<td class='tbicon'>イメージ</td>";
				str += "<td>物品名</td>";
				str += "<td>所在地</td>";
				str += "<td class='debug'>(x ,y)</td>";
				str += "<td class='destination'>納品先</td>";
				str += "<td class='regi'>登録</td>";
				str += "<td class='del'>削除</td>";
				str += "<td class='qr'>QRコード</td>";
				str += "<td class='battery'>電池残量</td>";
				str += "</tr></thead>";
				str += "<tbody>";

				int i = 0;
				int valLength = 10;
				for(i=0; i < ItemValue.length/valLength; i++){
					//Items.add( new Item( ItemValue[0+i*valLength], ItemValue[1+i*valLength], Double.valueOf(ItemValue[2+i*valLength]), Double.valueOf(ItemValue[3+i*valLength]), Double.valueOf(ItemValue[4+i*valLength]), ItemValue[5+i*valLength]  ) );
					//Items.get(i).setIco//rl(ItemValue[6+i*valLength]);
					
					//Window.alert(result);
					
					//List<Itema> Itemas = new ArrayList<Itema>();
					Item item = new Item(ItemValue[9+i*valLength], Double.valueOf(ItemValue[2+i*valLength]), Double.valueOf(ItemValue[3+i*valLength]), Double.valueOf(ItemValue[4+i*valLength]), 0.0, 0.0, 0.0, 0.0, ItemValue[5+i*valLength], Double.valueOf(ItemValue[8+i*valLength]), "0000");
					item.setIconUrl(ItemValue[6+i*valLength]);
					item.setKey(ItemValue[0+i*valLength]);
					item.setDestination(ItemValue[1+i*valLength]);
					item.setName(ItemValue[1+i*valLength]);
					Items.add(item);
					
					
					
//					Items.get(i).setIconUrl(ItemValue[6+i*valLength]);

					if(i%2==1)
						str += "<tr class=\"tablerecode1\">";
					else{
						str += "<tr class=\"tablerecode2\">";
					}
					str += "<td class='debug'>"+ item.getItemID() +"</td>";
					str += "<td class='tbicon'><img src=\""+ item.getIconUrl() +"\"></img></td>";
					//str += "<td><div style=\"font-size:150%;\">" + i + "</div></td>";
					str += "<td><a class='name' href=\"Ozvimesmap.html?"+ item.getKey() + "\">" + item.getName() +"</a></td>";
					str += "<td>"+ item.getAreaID() +"</td>";
					str += "<td class='debug'>("+item.getX()+ ", "+ item.getY()+ ")</td>";
					//tstr += "<td><input type=\"button\" value=\"削除\" itemkey=\"" + Items.get(j).getKey()  + "\" id=\"deleteButton"+ j +"\"></td>";
					
					str += "<td class='destination'>"+ item.getDestination() +"</td>";
					
					str += "<td class='regi'><div id=\"UpdateBtn"+ i +"\" align=\"center\" class=\"btn1\"></div></td>";
					str += "<td class='del'><div id=\"deleteBtn"+ i +"\" align=\"center\" class=\"btn1 del\"></div></td>";
					str += "<td class='qr'><img class='qrcode' src=\"https://chart.googleapis.com/chart?cht=qr&chs="+"150x150"+"&chco="+"3C14AC"+"&chl=http://ri Items.get(i)tern2014.appspot.com/Ozvimesmap.html?" + item.getKey()+"\" /img>";
					str += "<td class='battery'>"+ item.getBattery() +"</td>";//					str += "</tr>";
					
				}
					
					//Window.alert(str);
				
		//}
				
				str += "</tbody>";
				
				//Window.alert(str);
				
				//lbResult.setText(result);
				tableElement.setInnerHTML(str);
				
				

/*

				table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);


				//アイコンColoum
				Column<Item, String> imageColumn = new Column<Item, String>(new ImageCell()) {
					@Override
					public String getValue(Item object) {
						return object.getIconUrl();
					}
				};

				table.addColumn(imageColumn, "イメージ");


				// Name カラムを追加
				TextColumn<Item> nameColumn = new TextColumn<Item>(){
					@Override
					public String getValue(Item object) {
						return object.getName();
					}
				};       
				table.addColumn(nameColumn, "物品名");

				// Map カラムを追加
				TextColumn<Item> ageColumn = new TextColumn<Item>(){
					@Override
					public String getValue(Item object) {
						return object.getMapKey();
					}
				};       
				table.addColumn(ageColumn, "所在地");

				// Pos カラムを追加
				TextColumn<Item> posColumn = new TextColumn<Item>(){
					@Override
					public String getValue(Item object) {
						return "(" + Double.toString(object.getX())+ ", " + Double.toString(object.getY()) + ")";
					}
				};       
				table.addColumn(posColumn, "位置");

				
				final Column<Item, String> delColumn = new Column<Item, String>(new ButtonCell()) {
					@Override
					public String getValue(Item object) {						
						return "削除";
					}
				};

				delColumn.setFieldUpdater(new FieldUpdater<Item, String>() {
					@Override
					public void update(int index, Item object, String value) {
						
						service.DeleteItem(object.getKey(), new AsyncCallback() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert(caught.getMessage());
							}

							@Override
							public void onSuccess(Object result) {
								//Window.alert(result);
								ShowItems("");
							}
						});
						
					}
				});
				table.addColumn(delColumn, "削除");
				
				
			
				final Column<Item, String> updateColumn = new Column<Item, String>(new ButtonCell()) {
					@Override
					public String getValue(Item object) {						
						return "更新";
					}
				};
				
				//final UpdateDialogBox CellDiag = new UpdateDialogBox(Items.get(i).getKey(), Items.get(i).getName(), Items.get(i).getX(), Items.get(i).getY(), Items.get(i).getAccuracy());	//更新用D-Box
				
				updateColumn.setFieldUpdater(new FieldUpdater<Item, String>() {
					@Override
					public void update(int index, Item object, String value) {
						//CellDiag.show();
					}
				});
				table.addColumn(updateColumn, "更新");
				
				
				

				Column<Item, String> qrColumn = new Column<Item, String>(new ImageCell()) {
					@Override
					public String getValue(Item object) {
						return "https://chart.googleapis.com/chart?cht=qr&chs="+"150x150"+"&chco="+"DC143C"+"&chl=http://ozvimesmap.appspot.com/Ozvimesmap.html?" + object.getKey();
					}
				};
				table.addColumn(qrColumn, "QRコード");



				final SingleSelectionModel<Item> selectionModel = new SingleSelectionModel<Item>();
				table.setSelectionModel(selectionModel);
				selectionModel.addSelectionChangeHandler(
						new SelectionChangeEvent.Handler() {
							public void onSelectionChange(SelectionChangeEvent event) {
								Item selected = selectionModel.getSelectedObject();
								if(selected != null){
									//Window.alert(selected.name);
									
									String ItemUrl = "/Ozvimesmap.html?" + selected.getKey();
									//+ Items.get(i).getKey()
									Window.Location.replace(ItemUrl);

								}
							}
						});



				// データをセット
				table.setRowCount(Items.size(), true);
				table.setRowData(0, Items);

				RootPanel.get().add(table);
*/



				for(i=0; i < ItemValue.length/valLength; i++){
					Panel deletePanel = new VerticalPanel();
					Button deleteButton = new Button("");

					Panel UpdatePanel = new VerticalPanel();
					Button UpdateButton = new Button("登録");
					
					deleteButton.addStyleName("btn1 delbtn");
					UpdateButton.addStyleName("btn1 upbtn");

					final String keyVal = Items.get(i).getKey();
					final UpdateDialogBox diag = new UpdateDialogBox(keyVal, Items.get(i).getName(), Items.get(i).getX(), Items.get(i).getY(), Items.get(i).getOzvAcc(), Items.get(i).getAreaID());	//更新用D-Box

					deleteButton.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event){
							//Window.alert(keyVal);
							service.DeleteItem(keyVal, new AsyncCallback() {
								@Override
								public void onFailure(Throwable caught) {
								//	Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(Object result) {
									//Window.alert(result);
									ShowItems("",0);
								}
							});

						}
					});

					UpdateButton.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event){
							//Window.alert(keyVal);
							diag.show();
							diag.center();
						}
					});


					Panel mapPanel = new VerticalPanel();
					//mapPanel.setSize("640px", "400px");

					String BtnWidth = Integer.toString((int)(Window.getClientWidth()*0.1)) + "px";
					String BtnHeight = Integer.toString((int)(Window.getClientHeight()*0.1)) + "px";

					deleteButton.setSize(BtnWidth, BtnHeight);
					UpdateButton.setSize(BtnWidth, BtnHeight);

					String delname = "deleteBtn" + i;
					String upname = "UpdateBtn" + i;

					deletePanel.add(deleteButton);
					UpdatePanel.add(UpdateButton);
					RootPanel.get(delname).add(deletePanel);
					RootPanel.get(upname).add(UpdatePanel);
					
					readCB();
					
					hideLoader();
				}
			}
		});
	}



	private class UpdateDialogBox extends DialogBox {
		public UpdateDialogBox(String itemkey, String name, double x, double y, double Acc, final String mapname){
			setText("物品データの更新");
			Button closeButton = new Button("Cancel");
			Button sendButton = new Button("Send");

			closeButton.addStyleName("dlogbtn1");
			sendButton.addStyleName("dlogbtn1");

			closeButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});

			VerticalPanel panel = new VerticalPanel();
			HorizontalPanel hPane1 = new HorizontalPanel();
			HorizontalPanel hPane2 = new HorizontalPanel();
			HorizontalPanel hPane3 = new HorizontalPanel();
			HorizontalPanel hPane4 = new HorizontalPanel();
			HorizontalPanel hPane5 = new HorizontalPanel();

			//HTML sendMes = new HTML();
			//sendMes.setHTML("<h1>送る値</h1>");
			Label lab1 = new Label("名称 :");
			Label lab2 = new Label("[d]x :");
			Label lab3 = new Label("[d]y :");
			Label lab4 = new Label("[d]室内誤差 :");
			Label lab5 = new Label("納品先 :");
			Label lab6 = new Label("[d]エリア :");
			Label lab7 = new Label("物品画像 :");
			
			lab1.addStyleName("dlog-label");
			lab2.addStyleName("dlog-label");
			lab3.addStyleName("dlog-label");
			lab4.addStyleName("dlog-label");
			lab5.addStyleName("dlog-label");
			lab6.addStyleName("dlog-label");
			lab7.addStyleName("dlog-label");
			
			final TextBox nameBox = new TextBox();
			final TextBox xPosBox = new TextBox();
			final TextBox yPosBox = new TextBox();
			final TextBox AccBox = new TextBox();
			final TextBox desBox = new TextBox();
			
			nameBox.setText(name);
			xPosBox.setText(Double.toString(x));
			yPosBox.setText(Double.toString(y));
			AccBox.setText(Double.toString(Acc));
			
			
			hPane1.add(lab1);
			hPane1.add(nameBox);

			hPane2.add(lab2);
			hPane2.add(xPosBox);
			
			hPane3.add(lab3);
			hPane3.add(yPosBox);
			
			hPane4.add(lab4);
			hPane4.add(AccBox);
			
			hPane5.add(lab5);
			hPane5.add(desBox);

			panel.add(hPane1);
			panel.add(hPane2);
			panel.add(hPane3);
			panel.add(hPane4);
			panel.add(hPane5);
			
			final ListBox lb = new ListBox();
			
			HorizontalPanel hPane6 = new HorizontalPanel();
			
			hPane6.add(lab6);
			hPane6.add(lb);

			//	String[] mapList;

			service.getMapList(new AsyncCallback<ArrayList<String>>(){
				@Override
				public void onFailure(Throwable caught) {
//					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					int i = 0;

					for(i=0; i<result.size(); i++){
						lb.addItem(result.get(i));
						if( mapname.equals(result.get(i)) ){
							lb.setSelectedIndex(i);
						}
					}

					lb.setVisibleItemCount(1);
					
				}
			});
			
			lb.addStyleName("gwt-Textbox");

			// Add it to the root panel.
			panel.add(hPane6);

			final String iName = itemkey;
			

			HorizontalPanel hPane7 = new HorizontalPanel();
			
			final FormPanel form = new FormPanel();

			form.setEncoding(FormPanel.ENCODING_MULTIPART);
			form.setMethod(FormPanel.METHOD_POST);

			// Create a panel to hold all of the form widgets.
			VerticalPanel fPane = new VerticalPanel();
			form.setWidget(fPane);

			final Hidden key_box = new Hidden();
			key_box.setName("itemkey");
			key_box.setValue(itemkey);
			fPane.add(key_box);

			Hidden type_box = new Hidden();
			type_box.setName("type");
			type_box.setValue("icon");
			fPane.add(type_box);

			final FileUpload upload = new FileUpload();
			upload.setName("icon");
			fPane.add(upload);
			
			hPane7.add(lab7);
			hPane7.add(form);
			
			panel.add(hPane7);
			panel.add(sendButton);
			
			form.addSubmitHandler(new FormPanel.SubmitHandler() {
				public void onSubmit(SubmitEvent event) {
					showLoader();
					
					//Window.alert("Start Sending");
					//Window.alert(event.toString());
				}
			});
			form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
				public void onSubmitComplete(SubmitCompleteEvent event) {
					hide();
					Window.alert("Success"+event.getResults());

					ShowItems("",0);
				}
			});
			
			

			sendButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event){
					
					showLoader();
					
					String a = "[!\"#$%&'\\(\\)=~\\|`\\{\\+\\*\\}<>\\?_\\-\\^\\@\\[;:\\]\\,\\./]";
					Boolean isMatch = matches(a, nameBox.getValue());
					
					if(isMatch == true){
						hideLoader();
						Window.alert("! \" # $ % & ' ( ) = ~ | ` { + * } < > ? _ - ^ @ [ ; : ] , .  /\nこれらの記号は使用しないでください。");
						return;
					}
					else if(nameBox.getValue().equals("") || xPosBox.getValue().equals("") || yPosBox.getValue().equals("") || AccBox.getValue().equals("") || lb.getValue( lb.getSelectedIndex()).equals("") || desBox.getText().equals("") ){
						Window.alert("未記入の項目があります。\n記入してください。");
						hideLoader();
						return;
					}
					
					service.UpdateItem(iName, nameBox.getText(), Double.valueOf(xPosBox.getText()), Double.valueOf(yPosBox.getText()), Double.valueOf(AccBox.getText()), lb.getValue( lb.getSelectedIndex() ), desBox.getText(), new AsyncCallback<String>(){
							
						@Override
						public void onFailure(Throwable caught) {
							hideLoader();

							Window.alert("送信失敗\nエラーコード：" + caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							
							if(upload.getFilename() != ""){
//								Window.alert(upload.getFilename());
								form.setAction(result);
								form.submit();
							}	else{
								hide();
								ShowItems("",0);
							}
							
							//Window.alert(result);
						}
					});
				}

			});

			//panel.add(sendMes);

			//panel.add(new Label("Hello! DialogBox! This is a message."));
			panel.add(closeButton);
			setWidget(panel);
		}
	}

	private native static boolean matches(String strRegExp, String value) /*-{	
	var pattern = new RegExp(strRegExp);
	return value.search(pattern) != -1;
	}-*/;

	private native static void readCB() /*-{
		$wnd.disp();
	}-*/;
	
	private native static void hideLoader() /*-{
		$wnd.hideLoader();
	}-*/;
	
	private native static void showLoader() /*-{
		$wnd.showLoader();
	}-*/;
	
	public class Item {
		private String key;
		private String ItemID;
		private String name;
		private String APIDs;
		private String RSSIs;
		private double x;
		private double y;
		private double ozvAccuracy;
		private double latitude;
		private double longitude;
		private double floor;
		private double imesAccuracy;
		private String areaID;
		private String IconUrl;
		private double battery;
		private String destination;
		private String LastTime;
		private String BirthTime;
		
		public Item(String itemId, double x, double y, double oAcc, double lat, double lng, double floor, double iAcc, String areaID, double battery, String time) {
			this.ItemID = itemId;
			this.x = x;
			this.y = y;
			this.ozvAccuracy = oAcc;
			this.latitude = lat;
			this.longitude = lng;
			this.floor = floor;
			this.imesAccuracy = iAcc;
			this.areaID = areaID;
			this.battery = battery;
			this.LastTime = time;
		}

		public String getKey() {
			return this.key;
		}
		
		public String getItemID() {
			return this.ItemID;
		}

		public String getName() {
			return this.name;
		}
		
		public String getAPIDs() {
			return this.APIDs;
		}
		
		public String getRSSIs() {
			return this.RSSIs;
		}

		public double getX() {
			return this.x;
		}

		public double getY() {
			return this.y;
		}
		
		public double getOzvAcc() {
			return this.ozvAccuracy;
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
		
		public double getImesAcc() {
			return this.imesAccuracy;
		}
		
		public String getAreaID() {
			return this.areaID;
		}
		
		public String getIconUrl() {
			return IconUrl;
		}
		
		public double getBattery() {
			return this.battery;
		}
		
		public String getDestination() {
			return destination;
		}
		
		public String getTime() {
			return LastTime;
		}
		
		public String getBirthTime() {
			return BirthTime;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public void setItemID(String itemId) {
			this.ItemID = itemId;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void setAPIDs(String APIDs) {
			this.APIDs = APIDs;
		}
		
		public void setRSSIs(String RSSIs) {
			this.RSSIs = RSSIs;
		}

		public void setX(double x) {
			this.x = x;
		}

		public void setY(double y) {
			this.y = y;
		}
		
		public void setOzvAcc(double oAcc) {
			this.ozvAccuracy = oAcc;
		}
		
		public void setLat(double lat) {
			this.latitude = lat;;
		}

		public void setLng(double lng) {
			this.longitude = lng;
		}
		
		public void setFloor(double floor) {
			this.floor = floor;
		}
		
		public void setImesAcc(double iAcc) {
			this.imesAccuracy = iAcc;
		}
		
		public void setAreaID(String areaID) {
			this.areaID = areaID;
		}
		
		public void setIconUrl(String iconUrl) {
			this.IconUrl = iconUrl;
		}
		
		public void setBattery(double battery) {
			this.battery = battery;
		}
		
		public void setDestination(String des) {
			this.destination = des;
		}
		
		public void setTime(String time) {
			this.LastTime = time;
		}
		
		public void setBirthTime(String time) {
			this.BirthTime = time;
		}
	}
}