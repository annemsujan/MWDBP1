package edu.asu.mwdb.util;

import java.util.ArrayList;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.MinMax;
import edu.asu.mwdb.beans.SensorData;

public class NormalizeGesture {
	
	public static GestureOneDim normalize(GestureOneDim gesture){
		ArrayList<SensorData> data = gesture.getOneDimGesture();
		MinMax values = getMinMax(data);
		return normalizeData(data,values);
	}
	
	private static MinMax getMinMax(ArrayList<SensorData> data){
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for(int i=0;i<data.size();i++){
			SensorData sensor = data.get(i);
			ArrayList<Float> obs = sensor.getObservations();
			for(int j=0;j<obs.size();j++){
				if(min > obs.get(j))
					min = obs.get(j);
				if(max < obs.get(j))
					max = obs.get(j);
			}
		}
		MinMax obj = new MinMax(min,max);
		return obj;
	}
	
	private static GestureOneDim normalizeData(ArrayList<SensorData> data, MinMax values){
		GestureOneDim result = new GestureOneDim();
		for(int i=0;i<data.size();i++){
			SensorData sensor = data.get(i);
			SensorData newSensor = new SensorData();
			ArrayList<Float> obs = sensor.getObservations();
			for(int j=0;j<obs.size();j++){
				float value = obs.get(j);
				float newValue = (-1) + ( 2 * ((value-values.getMIN())/(values.getMAX()-values.getMIN())));
				newSensor.addObservation(newValue);
			}
			result.addSensorData(newSensor);
		}
		return result;
	}
}
