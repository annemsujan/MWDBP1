package edu.asu.mwdb.beans;

public class MinMax {
	private float MIN, MAX;

	public float getMIN() {
		return MIN;
	}

	public void setMIN(float mIN) {
		MIN = mIN;
	}

	public float getMAX() {
		return MAX;
	}

	public void setMAX(float mAX) {
		MAX = mAX;
	}
	
	public MinMax(float min, float max){
		this.MIN = min;
		this.MAX = max;
	}
}
