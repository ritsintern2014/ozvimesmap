package com.common.ozvimes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ImesArea {

	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key key;

	@Persistent
	private int AreaID;	//AreaのID
	
	@Persistent
	private double latitude;	//緯度

	@Persistent
	private double longitude;	//経度
	
	@Persistent
	private double floor;	//経度

	@Persistent
	private double accuracy;	//IMESの誤差

	public ImesArea( int areaID, double lat, double lng, double acc, double floor) {
		this.AreaID = areaID;
		this.latitude = lat;
		this.longitude = lng;
		this.accuracy = acc;
		this.floor = floor;
	}

	public Key getKey() {
		return this.key;
	}

	public int getAreaID() {
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
	
	public void setAreaID(int areaid) {
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