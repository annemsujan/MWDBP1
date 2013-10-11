package edu.asu.mwdb.util;

import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDistributionUtils {
	private static float DIST_MAX = 1.0f;
	private static NormalDistributionUtils instance;
	public static float MEAN,STD;
	public static int RESOLUTION;
	private static boolean initialized;
	private static double[] bands;
	
	public static NormalDistributionUtils getInstance(){
		if(instance==null){
			instance = new NormalDistributionUtils();
		}
		return instance;
	}
	
	protected NormalDistributionUtils(){
		instance.MEAN = Constants.MEAN;
		instance.STD = Constants.STD;
		instance.RESOLUTION = Constants.RESOLUTION;
		init();
	}
	
	public static char getBand(float value){
		short i=0;
		for(;i<instance.bands.length-1;i++){
			if(inBetween(value,instance.bands[i],instance.bands[i+1]))
				break;
		}
		return (char)(65+i);
	}
	
	private static boolean inBetween(float value, double min, double max){
		return (value<max && value>=min);
	}
	
	private static void init(){
		float width = DIST_MAX/instance.RESOLUTION;
		instance.bands = new double[2*(instance.RESOLUTION)+1];
		NormalDistribution dist = new NormalDistribution(instance.MEAN, instance.STD);
		double constantVal = dist.cumulativeProbability(0);
		for(int i=0;i<instance.RESOLUTION;i++){
			double firstVal = dist.cumulativeProbability(i*width);
			instance.bands[instance.RESOLUTION+i] = (firstVal-constantVal)*2;
			instance.bands[instance.RESOLUTION-i] = -(firstVal-constantVal)*2;
		}
		instance.bands[0]=-1;
		instance.bands[instance.RESOLUTION]=0;
		instance.bands[instance.bands.length-1]=1;
	}
}
