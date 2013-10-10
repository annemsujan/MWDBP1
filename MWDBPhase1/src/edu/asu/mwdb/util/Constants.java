package edu.asu.mwdb.util;

import java.awt.Color;

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
	
	// Heatmap Constants
	public static String HEATMAP_SAVE_LOC="/Users/kishanmaddula/Desktop/heatmap.png";
	public static int HEATMAP_BEGIN_VALUE=280;
	public static int HEATMAP_INCREMENT_VALUE=5;
	public static Color HEATMAP_HIGH_COLOR=Color.WHITE;
	public static Color HEATMAP_LOW_COLOR=Color.BLACK;
	
	public static void setNumberOfDocs(int count){
		NUMBER_OF_DOCS = count;
	}
	
	public static void setHighestIDF(float value){
		HIGHEST_IDF = value;
	}
}
