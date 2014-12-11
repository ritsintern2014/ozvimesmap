package com.list.ozvimes.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ListServiceAsync {
	void echoBack(String input, double x, double y, String MapKey, int column, int type, AsyncCallback<String> callback);
	void getMapList(AsyncCallback<ArrayList<String>> callback);
	void DeleteItem(String itemkey, AsyncCallback callback);
	void UpdateItem(String itemkey, String name, double x, double y, double Acc, String MapName, String dis, AsyncCallback<String> callback);
}
