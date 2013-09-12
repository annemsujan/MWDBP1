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
}
