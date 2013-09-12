package edu.asu.mwdb.util;

import java.util.ArrayList;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.SensorData;

public class GestureUtils {
	public static void printGesture(GestureOneDim dim){
		ArrayList<SensorData> data = dim.getOneDimGesture();
		for(int i=0;i<data.size();i++){
			SensorData sensor = data.get(i);
			printSensorData(sensor);
			System.out.println();
		}
	}
	
	public static void printSensorData(SensorData sensor){
		ArrayList<Float> data = sensor.getObservations();
		for(int i=0;i<data.size();i++){
			System.out.print(data.get(i) + "\t");
		}
	}
}
