package com.common.ozvimes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Item {

	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key key;
	
	@Persistent
	private String ItemID;

	@Persistent
	private String name;
	
	@Persistent
	private String APIDs;
	
	@Persistent
	private String RSSIs;

	@Persistent
	private double x;

	@Persistent
	private double y;

	@Persistent
	private double ozvAccuracy;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;

	@Persistent
	private double floor;

	@Persistent
	private double imesAccuracy;
	
	@Persistent
	private String areaID;
	
	@Persistent
	private String IconUrl;
	
	@Persistent
	private double battery;
	
	@Persistent
	private String destination;

	@Persistent
	private String LastTime;
	
	@Persistent
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

	public Key getKey() {
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