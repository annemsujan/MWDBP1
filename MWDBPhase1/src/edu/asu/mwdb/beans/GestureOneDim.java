package edu.asu.mwdb.beans;

import java.util.ArrayList;

public class GestureOneDim {
	private ArrayList<SensorData> oneDimGesture;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

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
