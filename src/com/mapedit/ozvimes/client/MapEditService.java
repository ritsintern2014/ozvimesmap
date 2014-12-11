package com.mapedit.ozvimes.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mapedit")
public interface MapEditService extends RemoteService {
	String echoBack(String name, double x, double y, double meter, double OWS);
	String getMapData(String name);
}
