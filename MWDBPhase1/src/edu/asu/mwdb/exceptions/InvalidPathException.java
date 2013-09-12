package edu.asu.mwdb.exceptions;

public class InvalidPathException extends Exception {
	
	public InvalidPathException(){
		System.out.println("Invalid path entered");
	}
	
	public InvalidPathException(String path){
		System.out.println("Invalid path entered: " + path);
	}
}
