package edu.asu.mwdb.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.SensorData;

public class LoopThroughAllFiles {
	public ArrayList<GestureOneDim> getAllGestures(String path){
		String[] dimensions = { "W", "X", "Y", "Z" };
		ArrayList<GestureOneDim> returnValues = new ArrayList<GestureOneDim>();
		for(String dimension : dimensions){
			String folderPath = path + "/" + dimension;
			System.out.println(folderPath);
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			for(int i=0;i<listOfFiles.length;i++){
				String fileName = listOfFiles[i].getName();
				if(fileName.endsWith("csv")){
					GestureOneDim oneDim = readFileData(folderPath+"/"+fileName);
					returnValues.add(oneDim);
				}
			}
		}
		return returnValues;
	}

	private GestureOneDim readFileData(String path){
		try{
			BufferedReader buf = new BufferedReader(new FileReader(path));
			String line;
			GestureOneDim gesture = new GestureOneDim();
			while((line = buf.readLine())!=null){
				String[] sensorsData = line.split(",");
				SensorData sensor = new SensorData();
				for(int i=0;i<sensorsData.length;i++){
					sensor.addObservation(Float.parseFloat(sensorsData[i]));
				}
				gesture.addSensorData(sensor);
			}
			return gesture;
		}
		catch(Exception e){
			return null;
		}
	}
}
