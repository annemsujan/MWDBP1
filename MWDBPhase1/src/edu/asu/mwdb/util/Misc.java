package edu.asu.mwdb.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.SingleGesture;

public class Misc {
	public static String getFileName(String filePath){
		int index = filePath.lastIndexOf("/");
		return filePath.substring(index+1);
	}
	
	public static String getOnlyFileName(String filePath){
		int index = filePath.lastIndexOf("/");
		String fullName = filePath.substring(index+1);
		index = fullName.indexOf(".");
		return fullName.substring(0, index);
	}
	
	public static HashMap<String,HashMap<String,GestureOneDim>> flattenGestures(HashMap<String,SingleGesture> gestures){
		Iterator iter = gestures.entrySet().iterator();
		HashMap<String,HashMap<String,GestureOneDim>> returnValues = new HashMap<String,HashMap<String,GestureOneDim>>();
		while(iter.hasNext()){
			Map.Entry pairs = (Map.Entry) iter.next();
			SingleGesture gesture = (SingleGesture) pairs.getValue();
			for(String dimension : Constants.DIMENSIONS){
				HashMap<String,GestureOneDim> dimValue = returnValues.get(dimension);
				if(dimValue==null){
					dimValue = new HashMap<String,GestureOneDim>();
				}
				dimValue.put(pairs.getKey().toString(), gesture.get(dimension));
				returnValues.put(dimension, dimValue);
			}
		}
		return returnValues;
	}
}
