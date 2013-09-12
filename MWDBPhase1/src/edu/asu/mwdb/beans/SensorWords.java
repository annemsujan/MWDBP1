package edu.asu.mwdb.beans;

import java.util.ArrayList;

public class SensorWords {
	ArrayList<String> words;
	
	public SensorWords(){
		words = new ArrayList<String>();
	}
	
	public void addWord(String word){
		words.add(word);
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
}
