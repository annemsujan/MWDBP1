package edu.asu.mwdb.matlab;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import edu.asu.mwdb.util.Constants;
import edu.asu.mwdb.util.NormalDistributionUtils;

public class MatlabUtils {

	private static MatlabUtils instance;
	public static MatlabProxyFactory factory;
	public static MatlabProxy proxy;

	public static MatlabUtils getInstance(){
		if(instance==null){
			instance = new MatlabUtils();
		}
		return instance;
	}

	public MatlabUtils() {
		try{
			instance.factory = new MatlabProxyFactory();
			instance.proxy = factory.getProxy();
			instance.proxy.eval(Constants.MATLAB_SCRIPTS_PATH);
		}
		catch(Exception e){
			System.out.println("Exception while initiating Matlab");
		}
	}
	
	public static void getSVD(){
		// Placeholder code..currently executing with default values in matlab
		// Placeholder for writing and reading from excel
		try {
			instance.proxy.eval("svdFunction");
		} catch (Exception e) {
			System.out.println("Exception while invoking SVD");
			e.printStackTrace();
		}
	}
	
	public static void getPCA(){
		// Placeholder code..currently executing with default values in matlab
		try {
			instance.proxy.eval("pcaFunction");
		} catch (Exception e) {
			System.out.println("Exception while invoking PCA");
			e.printStackTrace();
		}
	}
}
