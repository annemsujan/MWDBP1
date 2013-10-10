package edu.asu.mwdb.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.beans.SensorWord;
import edu.asu.mwdb.beans.SensorWords;
import edu.asu.mwdb.util.Constants;

public class TFIDFUtils {
	private static HashMap<String,HashMap<Integer,HashMap<Integer,Integer>>> idfHash = new HashMap<String, HashMap<Integer,HashMap<Integer,Integer>>>();
	private static HashMap<String,Float> idfValues;
	private static HashMap<String,HashMap<Integer,Float>> idfValues2;
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfValues = new HashMap<String, ArrayList<HashMap<String,Float>>>();
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfIdfValues = new HashMap<String, ArrayList<HashMap<String,Float>>>();
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfIdf2Values = new HashMap<String, ArrayList<HashMap<String,Float>>>();

	public static ArrayList<HashMap<String,Float>> getTFValues(String fileNum){
		return tfValues.get(fileNum);
	}

	public static ArrayList<HashMap<String,Float>> getIDFValues(String fileNum){
		return tfIdfValues.get(fileNum);
	}

	public static ArrayList<HashMap<String,Float>> getIDF2Values(String fileNum){
		return tfIdf2Values.get(fileNum);
	}

	// Given a gesture, generate only the TF values and return the TF values of the file
	public static ArrayList<HashMap<String,Float>> calculateOnlyTFValues(OneDGestureWords words){
		ArrayList<HashMap<String,Float>> result = new ArrayList<HashMap<String,Float>>();
		for(int i=0;i<words.getSensors().size();i++){
			SensorWords eachSensor = words.getSensor(i);
			HashMap<String,Float> map = new HashMap<String, Float>();
			for(int j=0;j<eachSensor.getWords().size();j++){
				String word = eachSensor.getWords().get(j);
				float value;
				if(!map.containsKey(word))
					value = 1;
				else
					value = map.get(word) + 1;
				map.put(word, value);
			}
			Iterator iter = map.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry pairs = (Map.Entry) iter.next();
				float value = Float.parseFloat(pairs.getValue().toString())/eachSensor.getWords().size();
				map.put(pairs.getKey().toString(), value);
			}
			result.add(map);
		}
		return result;
	}

	public static HashMap<String,Float> calculateTFValues(SensorWords data,int gestureNumber, int sensorNumber, String path){

		HashMap<String, Float> hashValues = new HashMap<String,Float>();
		ArrayList<String> strings = data.getWords();
		for(int i=0;i<strings.size();i++){
			if(hashValues.get(strings.get(i))!=null){
				float tempVal = hashValues.get(strings.get(i));
				hashValues.put(strings.get(i),tempVal+1);
			}
			else
				hashValues.put(strings.get(i), 1.0f);
			String word = strings.get(i);
			if(idfHash.get(word)!=null){
				HashMap<Integer,HashMap<Integer,Integer>> docMap = idfHash.get(word);
				if(docMap.get(gestureNumber)!=null){
					HashMap<Integer,Integer> sensorMap = docMap.get(gestureNumber);
					if(sensorMap.get(sensorNumber)==null)
						sensorMap.put(sensorNumber, 1);
					docMap.put(gestureNumber, sensorMap);
				}
				else{
					HashMap<Integer,Integer> sensorMap = new HashMap<Integer,Integer>();
					sensorMap.put(sensorNumber, 1);
					docMap.put(gestureNumber, sensorMap);
				}
				idfHash.put(word,docMap);
			}
			else{
				HashMap<Integer,Integer> sensorMap = new HashMap<Integer,Integer>();
				sensorMap.put(sensorNumber, 1);
				HashMap<Integer,HashMap<Integer,Integer>> docMap = new HashMap<Integer,HashMap<Integer,Integer>>();
				docMap.put(gestureNumber, sensorMap);
				idfHash.put(word, docMap);
			}
		}
		Iterator iter = hashValues.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry pairs = (Map.Entry) iter.next();
			float value = Float.parseFloat(pairs.getValue().toString())/strings.size();
			hashValues.put(pairs.getKey().toString(), value);
		}
		return hashValues;
	}

	// Function to generate TF IDF values
	public static void calculateTFIDF(){
		Iterator it = tfValues.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();
			ArrayList<HashMap<String,Float>> value = (ArrayList<HashMap<String,Float>>) pairs.getValue();
			ArrayList<HashMap<String,Float>> resultValue = new ArrayList<HashMap<String,Float>>();
			for(int i=0;i<value.size();i++){
				HashMap<String,Float> map = value.get(i);
				Iterator iter = map.entrySet().iterator();
				HashMap<String,Float> resultMap = new HashMap<String,Float>();
				while(iter.hasNext()){
					Map.Entry pair = (Map.Entry) iter.next();
					String innerKey = pair.getKey().toString();
					float idf = Float.parseFloat(pair.getValue().toString())*idfValues.get(innerKey);
					resultMap.put(innerKey, idf);
				}
				resultValue.add(resultMap);
			}
			tfIdfValues.put(key, resultValue);
		}
	}

	public static void calculateTFIDF2(){
		Iterator it = tfValues.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();
			ArrayList<HashMap<String,Float>> value = (ArrayList<HashMap<String,Float>>) pairs.getValue();
			ArrayList<HashMap<String,Float>> resultValue = new ArrayList<HashMap<String,Float>>();
			for(int i=0;i<value.size();i++){
				HashMap<String,Float> map = value.get(i);
				Iterator iter = map.entrySet().iterator();
				HashMap<String,Float> resultMap = new HashMap<String,Float>();
				while(iter.hasNext()){
					Map.Entry pair = (Map.Entry) iter.next();
					String innerKey = pair.getKey().toString();
					float idf = Float.parseFloat(pair.getValue().toString())*getIDF2(Integer.parseInt(key),innerKey);
					resultMap.put(innerKey, idf);
				}
				resultValue.add(resultMap);
			}
			tfIdf2Values.put(key, resultValue);
		}
	}

	// Print routines
	public static void printTFIDF(String fileNum){
		ArrayList<HashMap<String,Float>> value = tfIdfValues.get(fileNum);
		for(int i=0;i<value.size();i++){
			HashMap<String,Float> map = value.get(i);
			Iterator it = map.entrySet().iterator();
			System.out.print("Sensor " + i + "\t");
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry) it.next();
				System.out.print(pair.getKey().toString() + ": " + pair.getValue().toString() + "\t");
			}
			System.out.println();
		}
	}

	// Print routines
	public static void printTFIDF2(String fileNum){
		ArrayList<HashMap<String,Float>> value = tfIdf2Values.get(fileNum);
		for(int i=0;i<value.size();i++){
			HashMap<String,Float> map = value.get(i);
			Iterator it = map.entrySet().iterator();
			System.out.print("Sensor " + i + "\t");
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry) it.next();
				System.out.print(pair.getKey().toString() + ": " + pair.getValue().toString() + "\t");
			}
			System.out.println();
		}
	}

	public static ArrayList<HashMap<String,Float>> calculateTFValues(OneDGestureWords gesture, int gestureNumber, String path){

		ArrayList<SensorWords> sensors = gesture.getSensors();
		ArrayList<HashMap<String,Float>> tfResult = new ArrayList<HashMap<String,Float>>();
		for(int i=0;i<sensors.size();i++){
			tfResult.add(calculateTFValues(sensors.get(i),gestureNumber,i,path));
		}
		tfValues.put(path, tfResult);
		return tfResult;
	}

	public static void printTFValues(ArrayList<HashMap<String,Float>> map){
		for(int i=0;i<map.size();i++){
			System.out.print("Sensor " + i + ": ");
			HashMap<String,Float> hashValues = map.get(i);
			Iterator<Entry<String, Float>> it = hashValues.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Float> pairs = (Map.Entry<String,Float>) it.next();
				System.out.print(pairs.getKey() + ":" + pairs.getValue() + ",");
			}
			System.out.println();
		}
	}
	
	public static void printTFValues(String fileNum){
		printTFValues(tfValues.get(fileNum));
	}

	public static void calculateIDF(){
		idfValues=new HashMap<String,Float>();
		Iterator it = idfHash.entrySet().iterator();
		float highestIDF = Float.MIN_VALUE;
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			int totalSum = 0;
			HashMap<Integer,HashMap<Integer,Integer>> map = (HashMap<Integer,HashMap<Integer,Integer>>)pairs.getValue();
			Iterator gestureIterator = map.entrySet().iterator();
			while(gestureIterator.hasNext()){
				Map.Entry gesturePairs = (Map.Entry) gestureIterator.next();
				HashMap<Integer,Integer> sensorMap =  (HashMap<Integer,Integer>)(gesturePairs.getValue());
				totalSum += sensorMap.size();
			}
			float valueToInsert = (float)Math.log((float)Constants.NUMBER_OF_DOCS*Constants.NUMBER_OF_SENSORS/totalSum);
			if(valueToInsert>highestIDF)
				highestIDF = valueToInsert;
			idfValues.put((String)pairs.getKey(), valueToInsert);
		}
		Constants.setHighestIDF(highestIDF);
	}

	public static void calculateIDF2(){
		idfValues2 = new HashMap<String,HashMap<Integer,Float>>();
		Iterator it = idfHash.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			HashMap<Integer,HashMap<Integer,Integer>> map = (HashMap<Integer,HashMap<Integer,Integer>>) pairs.getValue();
			Iterator gestureIterator = map.entrySet().iterator();
			HashMap<Integer,Float> mainHash = new HashMap<Integer,Float>();
			while(gestureIterator.hasNext()){
				Map.Entry pair = (Map.Entry) gestureIterator.next();
				HashMap<Integer,Integer> sensorMap = (HashMap<Integer,Integer>)pair.getValue();
				mainHash.put(Integer.parseInt(pair.getKey().toString()), (float)Math.log((float)Constants.NUMBER_OF_SENSORS/sensorMap.size()));
			}
			idfValues2.put(pairs.getKey().toString(), mainHash);
		}
	}

	public static void printIDF2(){
		Iterator it = idfValues2.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.print(pairs.getKey().toString());
			HashMap<Integer,Integer> map = (HashMap<Integer,Integer>)pairs.getValue();
			Iterator gestureIterator = map.entrySet().iterator();
			while(gestureIterator.hasNext()){
				Map.Entry pair = (Map.Entry) gestureIterator.next();
				System.out.print(" " + pair.getKey().toString() + ": " + pair.getValue().toString());
			}
			System.out.println();
		}
	}

	public static float getIdfValue(String word){
		if(!idfValues.containsKey(word))
			return Float.NaN;
		return idfValues.get(word);
	}

	public static void printIDFValues(){
		Iterator it = idfValues.entrySet().iterator();
		System.out.println("Highest IDF Value " + Constants.HIGHEST_IDF);
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + ": " + pairs.getValue());
		}
	}

	public static ArrayList<SensorWord> getTopWords(OneDGestureWords selected, int choice,String fileNum){
		switch(choice){
		case 1 : return getGrayScaleTF(fileNum);
		case 2 : return getGrayScaleIDF(selected);
		case 3 : return getGrayScaleIDF2(selected,fileNum);
		case 4 : return getGrayScaleTFIDF(fileNum);
		case 5 : return getGrayScaleTFIDF2(fileNum);
		}
		return null;	
	}

	public static ArrayList<SensorWord> getGrayScaleTFIDF2(String fileNum){
		ArrayList<HashMap<String,Float>> selected = tfValues.get(fileNum);
		TreeMap<Float,ArrayList<SensorWord>> map = new TreeMap<Float,ArrayList<SensorWord>>();
		for(int i=0;i<selected.size();i++){
			HashMap<String,Float> innerMap = selected.get(i);
			Iterator it = innerMap.entrySet().iterator();

			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry) it.next();
				SensorWord word = new SensorWord(i,pairs.getKey().toString());
				float count = Float.parseFloat(pairs.getValue().toString())*getIDF2(Integer.parseInt(fileNum),pairs.getKey().toString());
				ArrayList<SensorWord> words;
				if(map.get(count)==null){
					words = new ArrayList<SensorWord>();
				}
				else{
					words = map.get(count);
				}
				words.add(word);
				map.put(count, words);
			}
		}
		return topValues(map,Constants.TOP_COUNT);

	}

	public static float getIDF2(int i, String s){
		HashMap<Integer,Float> value = idfValues2.get(s);
		return value.get(i);
	}

	public static ArrayList<SensorWord> getGrayScaleIDF2(OneDGestureWords selected, String fileNum){
		TreeMap<Float, ArrayList<SensorWord>> map = new TreeMap<Float, ArrayList<SensorWord>>();
		for(int i=0;i<selected.getSensors().size();i++){
			SensorWords words = selected.getSensor(i);
			for(int j=0;j<words.getWords().size();j++){
				String tempWord = words.getWords().get(j);
				SensorWord word = new SensorWord(i,tempWord);
				HashMap<Integer,Float> idfMap = idfValues2.get(tempWord);
				float value = idfMap.get(Integer.parseInt(fileNum));
				ArrayList<SensorWord> existing = map.get(value);
				if(existing==null)
					existing = new ArrayList<SensorWord>();
				existing.add(word);
				map.put(value, existing);

			}
		}
		return topValues(map, Constants.TOP_COUNT);
	}

	public static ArrayList<SensorWord> getGrayScaleIDF(OneDGestureWords selected) {
		TreeMap<Float, ArrayList<SensorWord>> map = new TreeMap<Float, ArrayList<SensorWord>>();
		for(int i=0;i<selected.getSensors().size();i++){
			SensorWords words = selected.getSensor(i);
			for(int j=0;j<words.getWords().size();j++){
				String tempWord = words.getWords().get(j);
				SensorWord word = new SensorWord(i,tempWord);
				ArrayList<SensorWord> existing = map.get(idfValues.get(tempWord));
				if(existing==null)
					existing = new ArrayList<SensorWord>();
				existing.add(word);
				map.put(idfValues.get(tempWord), existing);
			}
		}
		return topValues(map, Constants.TOP_COUNT);
	}

	public static ArrayList<SensorWord> getGrayScaleTFIDF(String fileNum){
		ArrayList<HashMap<String,Float>> selected = tfValues.get(fileNum);
		TreeMap<Float,ArrayList<SensorWord>> map = new TreeMap<Float,ArrayList<SensorWord>>();
		for(int i=0;i<selected.size();i++){
			HashMap<String,Float> innerMap = selected.get(i);
			Iterator it = innerMap.entrySet().iterator();

			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry) it.next();
				SensorWord word = new SensorWord(i,pairs.getKey().toString());
				float count = Float.parseFloat(pairs.getValue().toString())*idfValues.get(pairs.getKey().toString());
				ArrayList<SensorWord> words;
				if(map.get(count)==null){
					words = new ArrayList<SensorWord>();
				}
				else{
					words = map.get(count);
				}
				words.add(word);
				map.put(count, words);
			}
		}
		print(map);
		return topValues(map,Constants.TOP_COUNT);
	}

	private static void print(TreeMap<Float,ArrayList<SensorWord>> map){
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			Float key = Float.parseFloat(pairs.getKey().toString());
			ArrayList<SensorWord> value = (ArrayList<SensorWord>) pairs.getValue();
		}
	}

	public static ArrayList<SensorWord> getGrayScaleTF(String fileNum){
		ArrayList<HashMap<String,Float>> selected = tfValues.get(fileNum);
		TreeMap<Float,ArrayList<SensorWord>> map = new TreeMap<Float,ArrayList<SensorWord>>();
		for(int i=0;i<selected.size();i++){
			HashMap<String,Float> innerMap = selected.get(i);
			Iterator it = innerMap.entrySet().iterator();

			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry) it.next();
				SensorWord word = new SensorWord(i,pairs.getKey().toString());
				float count = Float.parseFloat(pairs.getValue().toString());
				ArrayList<SensorWord> words;
				if(map.get(count)==null){
					words = new ArrayList<SensorWord>();
				}
				else{
					words = map.get(count);
				}
				words.add(word);
				map.put(count, words);
			}
		}
		return topValues(map,Constants.TOP_COUNT);
	}

	public static ArrayList<SensorWord> topValues(TreeMap<Float,ArrayList<SensorWord>> map, int count){
		ArrayList<SensorWord> result = new ArrayList<SensorWord>();
		for(int i=0;i<count;){
			ArrayList<SensorWord> temp = map.get(map.lastKey());
			for(int j=0;j<temp.size() && i<count;j++){
				result.add(temp.get(j));
				i++;
			}
			map.remove(map.lastKey());
		}
		return result;
	}

}
