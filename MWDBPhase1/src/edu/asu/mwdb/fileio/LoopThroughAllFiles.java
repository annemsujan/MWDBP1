package edu.asu.mwdb.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.SensorData;
import edu.asu.mwdb.beans.SingleGesture;
import edu.asu.mwdb.util.Constants;
import edu.asu.mwdb.util.Misc;

public class LoopThroughAllFiles {
	public HashMap<String, SingleGesture> getAllGestures(String path){

		HashMap<String, GestureOneDim> returnValues = new HashMap<String, GestureOneDim>();
		String folderPath = path + "/";
		System.out.println(folderPath);
		File folder = new File(folderPath + Constants.DIMENSIONS[0]);
		File[] listOfFiles = folder.listFiles();
		Constants.setNumberOfDocs(listOfFiles.length);
		HashMap<String, SingleGesture> gestures = new HashMap<String,SingleGesture>();

		for(int i=0;i<listOfFiles.length;i++){
			SingleGesture gesture = new SingleGesture();
			String fileName = listOfFiles[i].getName();
			if(!Misc.getOnlyFileName(fileName).equals("")){
				for(String dimension:Constants.DIMENSIONS){
					if(fileName.endsWith("csv")){
						String filePath = folderPath + "/" + dimension +"/"+fileName;
						if(new File(filePath).exists()){
							GestureOneDim oneDim = readFileData(filePath);
							returnValues.put(fileName,oneDim);
							gesture.set(dimension, oneDim);
						}
					}
				}
				gestures.put(Misc.getFileName(fileName),gesture);
			}
			
		}
		return gestures;
	}

	public GestureOneDim readSingleFile(String path){
		GestureOneDim returnVal = new GestureOneDim();
		if(new File(path).exists()){
			returnVal = readFileData(path);
		}
		else {
			System.out.println("File not found");
			System.exit(1);
		}
		return returnVal;
	}

	private GestureOneDim readFileData(String path){
		try{
			BufferedReader buf = new BufferedReader(new FileReader(path));
			String line;
			GestureOneDim gesture = new GestureOneDim();
			gesture.setPath(Misc.getFileName(path));
			while((line = buf.readLine())!=null){
				String[] sensorsData = line.split(",");
				SensorData sensor = new SensorData();
				for(int i=0;i<sensorsData.length;i++){
					sensor.addObservation(Float.parseFloat(sensorsData[i]));
				}
				gesture.addSensorData(sensor);
			}
			buf.close();
			return gesture;
		}
		catch(Exception e){
			return null;
		}
	}
}
