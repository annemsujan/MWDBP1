package edu.asu.mwdb.util;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.beans.SensorData;
import edu.asu.mwdb.beans.SensorWords;

public class GenerateWords {
	int width, shift;
	
	public GenerateWords(int width, int shift){
		this.width = width;
		this.shift = shift;
	}
	
	public OneDGestureWords generateWords(GestureOneDim gesture){
		ArrayList<SensorData> sensors = gesture.getOneDimGesture();
		OneDGestureWords result = new OneDGestureWords();
		for(int i=0;i<sensors.size();i++){
			SensorWords words = generateSensorWords(sensors.get(i));
			result.addSensor(words);
		}
		return result;
	}
	
	private SensorWords generateSensorWords(SensorData data){
		ArrayList<Float> obs = data.getObservations();
		ArrayList<Character> chars = new ArrayList<Character>();
		NormalDistributionUtils dist = NormalDistributionUtils.getInstance();
		for(int i=0;i<obs.size();i++){
			chars.add(dist.getBand(obs.get(i)));
		}
		return generateWordsFromChars(chars);
	}
	
	private SensorWords generateWordsFromChars(ArrayList<Character> chars){
		char[] characters = new char[chars.size()];
		SensorWords result = new SensorWords();
		for(int i=0;i<chars.size();i++)
			characters[i] = chars.get(i);
		for(int i=0;i<chars.size();i+=shift){
			StringBuilder tempString = new StringBuilder();
			for(int j=0;j<width;j++){
				if((i+j)<chars.size())
					tempString.append(characters[i+j]);
			}
			result.addWord(tempString.toString());
		}
		return result;
	}
}
