package com.common.ozvimes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class AreaData {

	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key key;

	@Persistent
	private String name;
	
	@Persistent
	private String apIDs;
	
	@Persistent
	private double BPx;	//map上の基準点の位置

	@Persistent
	private double BPy;

	@Persistent
	private double unitM;	//１メートル

	@Persistent
	private double OriginWindowSize;	//設定時のウィンドウサイズ
	
	@Persistent
	private String path;	//アドレス
	

	public AreaData(String name, String apIDs) {
		this.name = name;
		this.apIDs = apIDs;
//		this.mapID = mapID;
	}

	public Key getKey() {
		return this.key;
	}
	
	public String getApIDs() {
		return this.apIDs;
	}
	
	public String getName() {
		return this.name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	
	public void setApIDs(String ids) {
		this.apIDs = ids;
	}
	
	//以降は地図関係
	
	public double getX() {
		return this.BPx;
	}

	public double getY() {
		return this.BPy;
	}

	public double getM() {
		return this.unitM;
	}

	public double getOWS() {
		return this.OriginWindowSize;
	}
	
	public String getPath() {
		return this.path;
	}

	public void setX(double BPx) {
		this.BPx = BPx;
	}

	public void setY(double BPy) {
		this.BPy = BPy;
	}

	public void setM(double M) {
		this.unitM = M;
	}

	public void setOWS(double OWS) {
		this.OriginWindowSize = OWS;
	}
	
	
	public void setPath(String path) {
		this.path = path;
	}

}