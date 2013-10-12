package edu.asu.mwdb.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.tc33.jheatchart.HeatChart;

import edu.asu.mwdb.beans.Rectangle;

public class DrawHeatChart {
	public static void drawHeatMap(double grayScale[][], ArrayList<Rectangle> rectangles){
		int initialVal = Constants.HEATMAP_BEGIN_VALUE;
		for(int i=0;i<rectangles.size();i++){
			Rectangle r = rectangles.get(i);
			for(int j=0;j<Constants.WINDOW_LENGTH;j++){
				if((r.getCol()+j)<grayScale[0].length){
					grayScale[r.getRow()][r.getCol()+j] = initialVal;
				}
				initialVal+=Constants.HEATMAP_INCREMENT_VALUE;
			}
		}
		HeatChart chart = new HeatChart(grayScale);
		
		chart.setHighValueColour(Constants.HEATMAP_HIGH_COLOR);
		chart.setLowValueColour(Constants.HEATMAP_LOW_COLOR);
		chart.setTitle("Heat map");
		chart.setXAxisLabel("Time");
		chart.setYAxisLabel("Sensor Number");
		chart.setColourScale(1);
		
		try {
			chart.saveToFile(new File(Constants.HEATMAP_SAVE_LOC));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
