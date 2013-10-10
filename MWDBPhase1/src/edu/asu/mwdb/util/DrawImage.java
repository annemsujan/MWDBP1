package edu.asu.mwdb.util;

import java.util.ArrayList;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.beans.Rectangle;
import edu.asu.mwdb.beans.SensorData;
import edu.asu.mwdb.beans.SensorWord;
import edu.asu.mwdb.beans.SensorWords;

public class DrawImage {
	public static void drawImage(GestureOneDim gesture, OneDGestureWords selected, ArrayList<SensorWord> words){
		ArrayList<SensorWords> sensorWords = selected.getSensors();
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		for(int i=0;i<sensorWords.size();i++){
			for(int j=0;j<words.size();j++){
				if(i==words.get(j).getRowNum()){
					ArrayList<String> rowWords = sensorWords.get(i).getWords();
					for(int k=0;k<rowWords.size();k++){
						if(rowWords.get(k).equals(words.get(j).getWord())){
							rectangles.add(new Rectangle(i,k*Constants.SHIFT_LENGTH));
							break;
						}
					}
				}
			}
		}
		int rows = gesture.getOneDimGesture().size();
		int cols = gesture.getOneDimGesture().get(0).getObservations().size();
		double[][] grayScale = new double[rows][cols];
		for(int i=0;i<rows;i++){
			SensorData row = gesture.getOneDimGesture().get(i);
			for(int j=0;j<cols;j++){
				float obs = row.getObservations().get(j);
				grayScale[i][j] = ((obs-Constants.MIN)/((Constants.MAX-Constants.MIN)/Constants.GRAYSCALE_VALS));
			}
		}
		DrawHeatChart.drawHeatMap(grayScale,rectangles);
		
//		JFrame f = new JFrame("Map");
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		DrawGraph dumm = new DrawGraph(grayScale,rectangles);
//        f.getContentPane().add(dumm);
//        f.setSize(800,200);
//        f.setLocation(200,200);
//        f.setVisible(true);
	}

}