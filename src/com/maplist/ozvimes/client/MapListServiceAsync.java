package com.maplist.ozvimes.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapListServiceAsync {
	void echoBack(String input, double x, double y, int type, AsyncCallback<String> callback);
	void DeleteMap(String blobkey, AsyncCallback callback);
	void getImesAreas(AsyncCallback<String> callback);
	void makeArea(String mapkey,String apIDs, String key, AsyncCallback<Integer> callback);
}
