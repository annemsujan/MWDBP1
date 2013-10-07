package edu.asu.mwdb.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.asu.mwdb.beans.Rectangle;

public class DrawGraph extends JPanel {
	BufferedImage image;
	Dimension size = new Dimension();
	int[][] colors;
	ArrayList<Rectangle> rectangles;
	int heightFactor = 10;
	int widthFactor = 20;

	public DrawGraph(int[][] grayScale, ArrayList<Rectangle> rectangles) {

		int height = grayScale.length*heightFactor;
		int width = grayScale[0].length*widthFactor;
		this.rectangles = rectangles;
		if(width>800){
			width = width/2;
			widthFactor/=2;
		}
		size.setSize(width, height);
		colors = grayScale;
		try {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			WritableRaster raster = image.getRaster();

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					raster.setSample(j,i,0,colors[i/heightFactor][j/widthFactor]);
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
		g.setColor(Color.GREEN);
		for(int i=0;i<rectangles.size();i++){
			int row = rectangles.get(i).getRow();
			int col = rectangles.get(i).getCol();
			g.drawRect(col*widthFactor, row*heightFactor, widthFactor*Constants.WINDOW_LENGTH, heightFactor);
		}
	}
}