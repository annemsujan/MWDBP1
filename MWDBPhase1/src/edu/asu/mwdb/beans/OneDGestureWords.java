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
	
	public ArrayList<SensorWords> getSensors(){
		return this.sensors;
	}
	
	public void print(){
		for(int i=0;i<sensors.size();i++){
			System.out.print("Sensor " + i + "\t");
			sensors.get(i).print();
		}
	}
}
