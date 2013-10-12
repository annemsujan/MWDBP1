package edu.asu.mwdb.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.SensorData;
import edu.asu.mwdb.util.Constants;
import edu.asu.mwdb.util.Misc;

public class LoopThroughAllFiles {
	public HashMap<String, GestureOneDim> getAllGestures(String path){
		String[] dimensions = { "Z" };
		HashMap<String, GestureOneDim> returnValues = new HashMap<String, GestureOneDim>();
		for(String dimension : dimensions){
			String folderPath = path + "/";
			System.out.println(folderPath);
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			Constants.setNumberOfDocs(listOfFiles.length);
			for(int i=0;i<listOfFiles.length;i++){
				String fileName = listOfFiles[i].getName();
				if(fileName.endsWith("csv")){
					GestureOneDim oneDim = readFileData(folderPath+"/"+fileName);
					returnValues.put(fileName,oneDim);
				}
			}
		}
		return returnValues;
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
			return gesture;
		}
		catch(Exception e){
			return null;
		}
	}
}
