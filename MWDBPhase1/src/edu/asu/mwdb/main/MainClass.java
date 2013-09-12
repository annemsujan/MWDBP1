package edu.asu.mwdb.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.exceptions.InvalidPathException;
import edu.asu.mwdb.fileio.LoopThroughAllFiles;
import edu.asu.mwdb.util.GenerateWords;
import edu.asu.mwdb.util.GestureUtils;
import edu.asu.mwdb.util.NormalDistributionUtils;
import edu.asu.mwdb.util.NormalizeGesture;
import edu.asu.mwdb.util.Validators;

public class MainClass {
	public static void main(String args[]){
		try{
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Use Defaults?");
			String option = buf.readLine();
			if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
				
			}
			else{
				System.out.println("Please enter the input folder: ");
				String inputPath = buf.readLine();
				if(!Validators.validatePath(inputPath))
					throw new InvalidPathException(inputPath);
				System.out.println("Standard Deviation: ");
				float stdDev = Float.parseFloat(buf.readLine());
				System.out.println("Window Length: ");
				int window = Integer.parseInt(buf.readLine());
				System.out.println("Shift Distance: ");
				int shift = Integer.parseInt(buf.readLine());
				System.out.println("Resolution: ");
				float resolution = Float.parseFloat(buf.readLine());

				NormalDistributionUtils.MEAN = 0;
				NormalDistributionUtils.STD = 0.25f;
				NormalDistributionUtils.RESOLUTION = 10;
				
				LoopThroughAllFiles loop = new LoopThroughAllFiles();
				ArrayList<GestureOneDim> gestures = loop.getAllGestures(inputPath);
				ArrayList<GestureOneDim> normalizedGestures = new ArrayList<GestureOneDim>();
				for(int i=0;i<gestures.size();i++){
					GestureOneDim normalized = NormalizeGesture.normalize(gestures.get(i));
					normalizedGestures.add(normalized);
					GenerateWords words = new GenerateWords(window, shift);
					OneDGestureWords actualWords = words.generateWords(normalized);
					System.out.println(actualWords.toString());
				}
				
				option = "Y";
				while(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					System.out.println("Do you want to enter input gesture file?");
					option = buf.readLine();
					if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					
					}
				}
				
				option = "Y";
				while(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					System.out.println("Do you want to enter data file?");
					option = buf.readLine();
					if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					
					}
				}
				
			}
		}
		catch(IOException e){
			System.out.println("Unable to read line");
		}
		catch(InvalidPathException e){
			System.out.println("Unable to parse the path");
		}
		catch(NumberFormatException e){
			System.out.println("Invalid number entered");
		}
	}
}
