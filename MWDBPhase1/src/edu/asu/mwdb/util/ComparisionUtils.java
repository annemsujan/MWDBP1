package edu.asu.mwdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.operations.TFIDFUtils;

public class ComparisionUtils {
	
	public static ArrayList<String> getSimilarTFDocs(GestureOneDim inputFile, ArrayList<String> fileNames){
		GestureOneDim normalized = NormalizeGesture.normalize(inputFile);
		GenerateWords words = new GenerateWords(Constants.WINDOW_LENGTH, Constants.SHIFT_LENGTH);
		OneDGestureWords actualWords = words.generateWords(normalized);
		ArrayList<HashMap<String,Integer>> tfVals = TFIDFUtils.calculateOnlyTFValues(actualWords);
		return getSimilarTFVectors(tfVals,fileNames);
	}
	
	public static ArrayList<String> getSimilarTFIDFDocs(GestureOneDim inputFile, ArrayList<String> fileNames){
		GestureOneDim normalized = NormalizeGesture.normalize(inputFile);
		GenerateWords words = new GenerateWords(Constants.WINDOW_LENGTH, Constants.SHIFT_LENGTH);
		OneDGestureWords actualWords = words.generateWords(normalized);
		ArrayList<HashMap<String,Integer>> tfVals = TFIDFUtils.calculateOnlyTFValues(actualWords);
		ArrayList<HashMap<String,Float>> tfIdfVals = new ArrayList<HashMap<String,Float>>();
		for(int i=0;i<tfVals.size();i++){
			HashMap<String,Integer> tfValsMap = tfVals.get(i);
			HashMap<String,Float> tfIdfValsMap = new HashMap<String,Float>();
			Iterator iter = tfValsMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry pairs = (Map.Entry) iter.next();
				String key = pairs.getKey().toString();
				float value = TFIDFUtils.getIdfValue(key);
				if(Float.isNaN(value))
					value = (float)Math.log(Constants.NUMBER_OF_DOCS*Constants.NUMBER_OF_SENSORS);
				float newVal = Integer.parseInt(pairs.getValue().toString())*value;
				tfIdfValsMap.put(key, newVal);
			}
			tfIdfVals.add(tfIdfValsMap);
		}
		return getSimilarTFIDFVectors(tfIdfVals,fileNames);
	}
	
	public static ArrayList<String> getSimilarTFIDF2Docs(GestureOneDim inputFile, ArrayList<String> fileNames){
		GestureOneDim normalized = NormalizeGesture.normalize(inputFile);
		GenerateWords words = new GenerateWords(Constants.WINDOW_LENGTH, Constants.SHIFT_LENGTH);
		OneDGestureWords actualWords = words.generateWords(normalized);
		ArrayList<HashMap<String,Integer>> tfVals = TFIDFUtils.calculateOnlyTFValues(actualWords);
		ArrayList<HashMap<String,Float>> tfIdf2Vals = new ArrayList<HashMap<String,Float>>();
		HashMap<String,Float> idf2Vals = new HashMap<String,Float>();
		
		for(int i=0;i<tfVals.size();i++){
			HashMap<String,Integer> tfMap = tfVals.get(i);
			Iterator iter = tfMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry pairs = (Map.Entry) iter.next();
				String key = pairs.getKey().toString();
				float value=0;
				if(idf2Vals.containsKey(key))
					value = idf2Vals.get(key);
				value++;
				idf2Vals.put(key, value);
			}
		}
		Iterator iter = idf2Vals.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry pairs = (Map.Entry) iter.next();
			String key = pairs.getKey().toString();
			float value = (float)Math.log(Constants.NUMBER_OF_SENSORS/Float.parseFloat(pairs.getValue().toString()));
			idf2Vals.put(key, value);
		}
		
		for(int i=0;i<tfVals.size();i++){
			HashMap<String,Integer> tfValsMap = tfVals.get(i);
			HashMap<String,Float> tfIdfValsMap = new HashMap<String,Float>();
			iter = tfValsMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry pairs = (Map.Entry) iter.next();
				String key = pairs.getKey().toString();
				float value = idf2Vals.get(key);
				float newVal = Integer.parseInt(pairs.getValue().toString())*value;
				tfIdfValsMap.put(key, newVal);
			}
			tfIdf2Vals.add(tfIdfValsMap);
		}
		return getSimilarTFIDF2Vectors(tfIdf2Vals,fileNames);
	}
	
	private static ArrayList<String> getSimilarTFIDF2Vectors(ArrayList<HashMap<String,Float>> tfVals, ArrayList<String> fileNames){
		TreeMap<Float, ArrayList<String>> simMap = new TreeMap<Float,ArrayList<String>>();
		for(int i=0;i<fileNames.size();i++){
			float value = vectorSimilarity(tfVals,TFIDFUtils.getIDF2Values(fileNames.get(i)),0);
			ArrayList<String> tempList = simMap.get(value);
			if(simMap.get(value)==null)
				tempList = new ArrayList<String>();
			tempList.add(fileNames.get(i));
			simMap.put(value, tempList);
		}
		return topKValues(simMap,Constants.TOP_COUNT);
	}
	
	private static ArrayList<String> getSimilarTFIDFVectors(ArrayList<HashMap<String,Float>> tfVals, ArrayList<String> fileNames){
		TreeMap<Float, ArrayList<String>> simMap = new TreeMap<Float,ArrayList<String>>();
		for(int i=0;i<fileNames.size();i++){
			float value = vectorSimilarity(tfVals,TFIDFUtils.getIDFValues(fileNames.get(i)),0);
			ArrayList<String> tempList = simMap.get(value);
			if(simMap.get(value)==null)
				tempList = new ArrayList<String>();
			tempList.add(fileNames.get(i));
			simMap.put(value, tempList);
		}
		return topKValues(simMap,Constants.TOP_COUNT);
	}
	
	private static float vectorSimilarity(ArrayList<HashMap<String,Float>> query, ArrayList<HashMap<String,Float>> file,int flag){
		float totalSum = 0;
		for(int i=0;i<query.size();i++){
			totalSum+= dotProduct(query.get(i),file.get(i),flag)/(magnitude(query.get(i),flag)*magnitude(file.get(i),flag));
		}
		return totalSum;
	}
	
	private static float dotProduct(HashMap<String,Float> query, HashMap<String,Float> file,int flag){
		float totalSum = 0;
		Iterator it = query.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();
			float value = Float.parseFloat(pairs.getValue().toString());
			if(file.get(key)!=null)
				totalSum += (file.get(key)*value);
		}
		return totalSum;
	}
	
	private static ArrayList<String> getSimilarTFVectors(ArrayList<HashMap<String,Integer>> tfVals, ArrayList<String> fileNames){
		TreeMap<Float, ArrayList<String>> simMap = new TreeMap<Float,ArrayList<String>>();
		for(int i=0;i<fileNames.size();i++){
			float value = vectorSimilarity(tfVals,TFIDFUtils.getTFValues(fileNames.get(i)));
			ArrayList<String> tempList = simMap.get(value);
			if(simMap.get(value)==null)
				tempList = new ArrayList<String>();
			tempList.add(fileNames.get(i));
			simMap.put(value, tempList);
		}
		return topKValues(simMap,Constants.TOP_COUNT);
	}
	
	private static float vectorSimilarity(ArrayList<HashMap<String,Integer>> query, ArrayList<HashMap<String,Integer>> file){
		float totalSum = 0;
		for(int i=0;i<query.size();i++){
			totalSum+= dotProduct(query.get(i),file.get(i))/(magnitude(query.get(i))*magnitude(file.get(i)));
		}
		return totalSum;
	}
	
	private static float dotProduct(HashMap<String,Integer> query, HashMap<String,Integer> file){
		float totalSum = 0;
		Iterator it = query.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();
			float value = Float.parseFloat(pairs.getValue().toString());
			if(file.get(key)!=null)
				totalSum += (file.get(key)*value);
		}
		return totalSum;
	}
	
	private static double magnitude(HashMap<String,Integer> vector){
		float sum = 0;
		Iterator it = vector.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			float value = Float.parseFloat(pairs.getValue().toString());
			sum += (value*value);
		}
		return Math.sqrt(sum);
	}
	
	private static double magnitude(HashMap<String,Float> vector, int flag){
		float sum = 0;
		Iterator it = vector.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			float value = Float.parseFloat(pairs.getValue().toString());
			sum += (value*value);
		}
		return Math.sqrt(sum);
	}
	
	private static ArrayList<String> topKValues(TreeMap<Float,ArrayList<String>> map, int count){
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0;i<count;){
			ArrayList<String> topMap = map.get(map.lastKey());
			for(int j=0;j<topMap.size();j++){
				result.add(topMap.get(j));
				i++;
				if(i==count-1)
					break;
			}
			map.remove(map.lastKey());
		}
		return result;
	}
}
