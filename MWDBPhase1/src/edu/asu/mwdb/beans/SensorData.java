package edu.asu.mwdb.beans;

import java.util.ArrayList;

public class SensorData {
	private ArrayList<Float> observations;
	
	public SensorData(){
		observations = new ArrayList<Float>();
	}
	
	public void addObservation(float obs){
		observations.add(obs);
	}

	public ArrayList<Float> getObservations() {
		return observations;
	}

	public void setObservations(ArrayList<Float> observations) {
		this.observations = observations;
	}
}
