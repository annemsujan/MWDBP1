package edu.asu.mwdb.beans;

public class SensorWord {
	int rowNum;
	String word;
	
	public SensorWord(int i, String s){
		this.rowNum = i;
		this.word = s;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public void print(){
		System.out.print("\t" + word);
	}
}
