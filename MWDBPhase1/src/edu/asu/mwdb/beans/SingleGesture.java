package edu.asu.mwdb.beans;

public class SingleGesture {
	private GestureOneDim w,x,y,z;

	public GestureOneDim getW() {
		return w;
	}

	public void setW(GestureOneDim w) {
		this.w = w;
	}

	public GestureOneDim getX() {
		return x;
	}

	public void setX(GestureOneDim x) {
		this.x = x;
	}

	public GestureOneDim getY() {
		return y;
	}

	public void setY(GestureOneDim y) {
		this.y = y;
	}

	public GestureOneDim getZ() {
		return z;
	}

	public void setZ(GestureOneDim z) {
		this.z = z;
	}

	public SingleGesture(GestureOneDim w, GestureOneDim x, GestureOneDim y, GestureOneDim z){
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public SingleGesture(){
		this.w = new GestureOneDim();
		this.x = new GestureOneDim();
		this.y = new GestureOneDim();
		this.z = new GestureOneDim();
	}

	public void set(String dimension,GestureOneDim dim){
		switch(dimension){
		case "X" : this.setX(dim);
		break;
		case "Y" : this.setY(dim);
		break;
		case "Z" : this.setZ(dim);
		break;
		case "W" : this.setW(dim);
		break;
		}
	}
	
	public GestureOneDim get(String dimension){
		switch(dimension){
		case "X" : return this.getX();
		case "Y" : return this.getY();
		case "Z" : return this.getZ();
		case "W" : return this.getW();
		}
		return null;
	}
}
