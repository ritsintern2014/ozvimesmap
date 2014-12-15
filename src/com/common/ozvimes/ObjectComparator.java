package com.common.ozvimes;

import java.util.Comparator;

public class ObjectComparator implements Comparator<Item> {

	int ASC = 1;
	int DESC = -1;
	int sort = DESC;
	
	@Override
	public int compare(Item p1, Item p2) {
//		return p1.getName().compareTo(p2.getName()) * sort;
		
		return p1.getAreaID().compareTo(p2.getAreaID()) * sort;
	}
	/*
	public int destinationCompare(Item p1, Item p2) {
		return p1.getDestination().compareTo(p2.getDestination()) * sort;
	}
	
	public int areaCompare(Item p1, Item p2) {
		return p1.getAreaID().compareTo(p2.getAreaID()) * sort;
	}

	public int batteryCompare(Item p1, Item p2) {
		if(p1.getBattery() - p2.getBattery() == 0){
			return 0;
		}
		else if(p1.getBattery() - p2.getBattery() == 1){
			return -1 * sort;
		}
		else if(p1.getBattery() - p2.getBattery() == -1){
			return 1 * sort;
		}
		
		return 0;
	}
	
	public int idCompare(Item p1, Item p2) {
		return p1.getItemID().compareTo(p2.getItemID()) * sort;
	}*/
}
