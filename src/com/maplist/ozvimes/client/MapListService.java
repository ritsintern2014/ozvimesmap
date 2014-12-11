package com.maplist.ozvimes.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MapList")
public interface MapListService extends RemoteService {
	String echoBack(String input, double x, double y, int type);
	void DeleteMap(String blobkey);
	String getImesAreas();
	Integer makeArea(String mapkey, String key,String apIDs);
}
