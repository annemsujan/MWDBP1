package edu.asu.mwdb.beans;

import java.util.ArrayList;

public class OneDGestureWords {
	ArrayList<SensorWords> sensors;
	
	public OneDGestureWords(){
		sensors = new ArrayList<SensorWords>();
	}
	
	public void addSensor(SensorWords words){
		sensors.add(words);
	}
	
	public SensorWords getSensor(int index){
		return sensors.get(index);
	}
}
