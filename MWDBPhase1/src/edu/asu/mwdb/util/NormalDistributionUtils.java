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
			instance = new NormalDistributionUtils(MEAN,STD,RESOLUTION);
		}
		return instance;
	}
	
	protected NormalDistributionUtils(float mean, float std, int resolution){
		instance.MEAN = mean;
		instance.STD = std;
		instance.RESOLUTION = resolution;
		instance.initialized = false;
	}
	
	public static char getBand(float value){
		if(!initialized)
			init();
		short i=0;
		for(;i<instance.RESOLUTION-1;i++){
			if(inBetween(Math.abs(value),bands[i],bands[i+1]))
				break;
		}
		if(value>0)
			return (char)('A'+i);
		else
			return (char)('a'+i);
	}
	
	private static boolean inBetween(float value, double min, double max){
		return (value<max && value>=min);
	}
	
	private static void init(){
		float width = DIST_MAX/instance.RESOLUTION;
		instance.bands = new double[instance.RESOLUTION];
		NormalDistribution dist = new NormalDistribution(instance.MEAN, instance.STD);
		double constantVal = dist.cumulativeProbability(0);
		for(int i=0;i<instance.RESOLUTION;i++){
			double firstVal = dist.cumulativeProbability(i*width);
			instance.bands[i] = (firstVal-constantVal)*2;
		}
	}
}
