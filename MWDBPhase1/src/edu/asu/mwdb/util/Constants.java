package edu.asu.mwdb.util;

public class Constants {
	public static int NUMBER_OF_SENSORS = 20;
	public static int NUMBER_OF_DOCS;
	public static float HIGHEST_IDF;
	public static int TOP_COUNT = 10;
	public static int GRAYSCALE_VALS = 255;
	public static float MAX = 1.0f;
	public static float MIN = -1.0f;
	public static int WINDOW_LENGTH;
	public static int SHIFT_LENGTH;
	
	public static void setNumberOfDocs(int count){
		NUMBER_OF_DOCS = count;
	}
	
	public static void setHighestIDF(float value){
		HIGHEST_IDF = value;
	}
}
