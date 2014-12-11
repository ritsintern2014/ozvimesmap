package com.mapedit.ozvimes.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapEditServiceAsync {
	void echoBack(String name, double x, double y, double meter, double OWS, AsyncCallback<String> callback);
	void getMapData(String name, AsyncCallback<String> callback);
}