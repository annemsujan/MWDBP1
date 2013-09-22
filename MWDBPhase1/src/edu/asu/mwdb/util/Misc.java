package edu.asu.mwdb.util;

public class Misc {
	public static String getFileName(String filePath){
		int index = filePath.lastIndexOf("/");
		return filePath.substring(index+1);
	}
	
	public static String getOnlyFileName(String filePath){
		int index = filePath.lastIndexOf("/");
		String fullName = filePath.substring(index+1);
		index = fullName.indexOf(".");
		return fullName.substring(0, index);
	}
}
