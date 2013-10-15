package edu.asu.mwdb.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.beans.SensorWord;
import edu.asu.mwdb.beans.SensorWords;
import edu.asu.mwdb.util.Constants;

public class TFIDFHeatMapUtils {
	
	private static HashMap<String,Float> idfValues;
	private static HashMap<String,HashMap<Integer,Float>> idfValues2;
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfValues = new HashMap<String, ArrayList<HashMap<String,Float>>>();
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfIdfValues = new HashMap<String, ArrayList<HashMap<String,Float>>>();
	private static HashMap<String, ArrayList<HashMap<String,Float>>> tfIdf2Values = new HashMap<String, ArrayList<HashMap<String,Float>>>();

	public static void setValues(HashMap<String,Float> idfVals, HashMap<String,HashMap<Integer,Float>> idfVals2,HashMap<String, ArrayList<HashMap<String,Float>>> tfVals,
			HashMap<String, ArrayList<HashMap<String,Float>>> tfIdfVals,HashMap<String, ArrayList<HashMap<String,Float>>> tfIdf2Vals){
		idfValues = idfVals;
		idfValues2 = idfVals2;
		tfValues=tfVals;
		tfIdfValues = tfIdfVals;
		tfIdf2Values = tfIdf2Vals;
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
	
	public static float getIDF2(int i, String s){
		HashMap<Integer,Float> value = idfValues2.get(s);
		return value.get(i);
	}

}
