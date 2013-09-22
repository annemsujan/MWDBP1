package edu.asu.mwdb.beans;

public class Rectangle {
	int row, col;
	
	public Rectangle (int i, int j){
		row = i;
		col = j;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
}
