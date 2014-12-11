package com.mapedit.ozvimes.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;



public class MapEdit implements EntryPoint {

	private final MapEditServiceAsync service = GWT.create(MapEditService.class);
	private Button MapAdmin = new Button("地図");
	
	@Override
	public void onModuleLoad() {		
		//クリックで地図リスト
		this.MapAdmin.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("/MapList.html");
			}
		});
		RootPanel.get("MapAdmin").add(MapAdmin);	
		
		Button sendButton = new Button("地図送信");
/*
		final TextBox nameBox = new TextBox();
		nameBox.setText("地図の名前");
	*/	

		MapAdmin.addStyleName("btn2");
		sendButton.addStyleName("btn2");

//		RootPanel.get("nameBox").add(nameBox);
		RootPanel.get("sendButton").add(sendButton);

		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a TextBox, giving it a name so that it will be submitted.
		/*
	    final TextBox tb = new TextBox();
	    tb.setName("textBoxFormElement");
	    panel.add(tb);*/

		// Create a ListBox, giving it a name and some values to be associated with
		// its options.
		/*
	    ListBox lb = new ListBox();
	    lb.setName("listBoxFormElement");
	    lb.addItem("foo", "fooValue");
	    lb.addItem("bar", "barValue");
	    lb.addItem("baz", "bazValue");
	    panel.add(lb);*/

		// Create a FileUpload widget.
		final FileUpload upload = new FileUpload();
		upload.setName("map");
		panel.add(upload);
		upload.setStyleName("imageup");
		
		/*
		final MyDialogBox canv = new MyDialogBox();

		upload.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				canv.show();
				
			}			
		});
		*/
		
		final Hidden name_box = new Hidden();
	    name_box.setName("name");
	    panel.add(name_box);
		
		final Hidden x_box = new Hidden();
	    x_box.setName("xpos");
	    panel.add(x_box);
	    
	    final Hidden y_box = new Hidden();
	    y_box.setName("ypos");
	    panel.add(y_box);
	    
	    final Hidden meter_box = new Hidden();
	    meter_box.setName("meter");
	    panel.add(meter_box);
	    
	    final Hidden OWS_box = new Hidden();
	    OWS_box.setName("OWS");
	    panel.add(OWS_box);
	    
	    final Hidden areaKey_box = new Hidden();
	    areaKey_box.setName("areaKey");
	    panel.add(areaKey_box);
	    
	    final Hidden type_box = new Hidden();
	    type_box.setName("type");
		type_box.setValue("map");
	    panel.add(type_box);
		
	    final Hidden onChange_box = new Hidden();
	    onChange_box.setName("onChangeMap");
	    onChange_box.setValue("false");
	    onChange_box.setID("onChangeMap");
	    panel.add(onChange_box);
		
		sendButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				final double xpos = Double.valueOf(RootPanel.get("BPx").getElement().getPropertyString("value"));
				final double ypos = Double.valueOf(RootPanel.get("BPy").getElement().getPropertyString("value"));
				final double meter = Double.valueOf(RootPanel.get("unitM").getElement().getPropertyString("value"));
				final double OWS = Double.valueOf(RootPanel.get("OWS").getElement().getPropertyString("value"));
				
//				name_box.setValue(nameBox.getText());
				
				//マップデータ登録済みであれば
				x_box.setValue(RootPanel.get("BPx").getElement().getPropertyString("value"));
				y_box.setValue(RootPanel.get("BPy").getElement().getPropertyString("value"));
				meter_box.setValue(RootPanel.get("unitM").getElement().getPropertyString("value"));
				OWS_box.setValue(RootPanel.get("OWS").getElement().getPropertyString("value"));
				
				areaKey_box.setValue(RootPanel.get("areaKey").getElement().getPropertyString("value"));
				

				service.echoBack("nameBox.getText()", xpos, ypos, meter, OWS, new AsyncCallback<String>(){	//
					@Override
					public void onFailure(Throwable caught) {
						//lbResult.setText(caught.getMessage());
					}

					
					@Override
					public void onSuccess(String result) {
						//Window.alert(result);
						//form.getElement().setPropertyString("name", nameBox.getText());
						
						if(RootPanel.get("inmap").getElement().getPropertyString("src") != ""){	//未選択でなければ送信
							form.setAction(result);
							form.submit();
						} else{
							Window.alert("ファイルを選択してください");
						}
/*
						final Image image = new Image();

						// Point the image at a real URL.
						image.setUrl(result);

						RootPanel.get().add(image);

						HTML html = new HTML("<img src=\""+result+"\"");

						RootPanel.get().add(html);*/
					}
				});	
			}



		});		

		// Add a 'submit' button.
		/*
	    panel.add(new Button("Submit", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    }));*/

		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
				/*
	        if (tb.getText().length() == 0) {
	          Window.alert("The text box must not be empty");
	          event.cancel();
	        }*/
			}
		});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this event is
				// fired. Assuming the service returned a response of type text/html,
				// we can get the result text here (see the FormPanel documentation for
				// further explanation).
				Window.alert("Success"+event.getResults());
				Window.Location.replace("/MapList.html");
			}
		});
		
		RootPanel.get("formPanel").add(form);
		

		//マップデータの有無
		String mapname = "";
		
		while(true){
			mapname = RootPanel.get("areaKey").getElement().getPropertyString("value");
			
			if(! RootPanel.get("areaKey").getElement().getPropertyString("value").equals("none")){
				service.getMapData(mapname, new AsyncCallback<String>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.toString());
					}

					@Override
					public void onSuccess(String result) {
						
						//Window.alert(result+":::");
						
						String mapValue[] = result.split(",",0);
						
						RootPanel.get("BPx").getElement().setPropertyString("value", mapValue[0]);
						RootPanel.get("BPy").getElement().setPropertyString("value", mapValue[1]);
						RootPanel.get("unitM").getElement().setPropertyString("value", mapValue[2]);
						RootPanel.get("OWS").getElement().setPropertyString("value", mapValue[3]);
						RootPanel.get("inmap").getElement().setPropertyString("src", mapValue[4]);
						
						RootPanel.get("JavaRead").getElement().setPropertyString("value", "true");
						
						//callJS();
					}
				});
				
				break;
			}
		}
	}
	
	public native void callJS() /*-{
		CalledJava();
	}-*/;
	
	private class MyDialogBox extends DialogBox {
		public MyDialogBox(){
			setText("地図の設定");
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

			Image a = new Image();
			a.getElement().setId("inmap");

			Canvas canvas = Canvas.createIfSupported();
			canvas.getElement().setId("can");
			
			panel.add(a);
			panel.add(canvas);
			
			panel.add(closeButton);
			setWidget(panel);
		}
	}
	
	public native String getParam() /*-{
	  return window.location.search.substring(1);
	}-*/;
}