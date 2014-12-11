package com.list.ozvimes.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("listpage")
public interface ListService extends RemoteService {
	String echoBack(String input, double x, double y, String MapKey, int Column, int type);
	ArrayList<String> getMapList();
	void DeleteItem(String itemkey);
	String UpdateItem(String itemkey, String name, double x, double y, double Acc, String dis, String MapName);
}
