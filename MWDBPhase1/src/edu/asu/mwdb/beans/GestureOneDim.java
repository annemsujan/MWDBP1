package edu.asu.mwdb.beans;

import java.util.ArrayList;

public class GestureOneDim {
	private ArrayList<SensorData> oneDimGesture;
	
	public GestureOneDim(){
		oneDimGesture = new ArrayList<SensorData>();
	}
	
	public void addSensorData(SensorData data){
		oneDimGesture.add(data);
	}

	public ArrayList<SensorData> getOneDimGesture() {
		return oneDimGesture;
	}

	public void setOneDimGesture(ArrayList<SensorData> oneDimGesture) {
		this.oneDimGesture = oneDimGesture;
	}
}
