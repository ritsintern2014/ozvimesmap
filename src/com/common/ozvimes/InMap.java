package com.common.ozvimes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class InMap {

	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key key;

	@Persistent
	private String name;

	@Persistent
	private double BPx;	//map上の基準点の位置

	@Persistent
	private double BPy;

	@Persistent
	private double unitM;	//１メートル

	@Persistent
	private double OriginWindowSize;	//設定時のウィンドウサイズ
	
	@Persistent
	private int AreaID;	//エリアIDに対応させる
	
	@Persistent
	private String path;	//アドレス

	public InMap( String name, double BPx, double BPy, double meter, double OWS, String path) {
		this.name = name;
		this.BPx = BPx;
		this.BPy = BPy;
		this.unitM = meter;
		this.OriginWindowSize = OWS;
		this.path = path;
	}

	public Key getKey() {
		return this.key;
	}

	public String getName() {
		return this.name;
	}

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
	
	public double getAreaID() {
		return this.AreaID;
	}
	
	public String getPath() {
		return this.path;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public void setAreaID(int AreaID) {
		this.AreaID = AreaID;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}