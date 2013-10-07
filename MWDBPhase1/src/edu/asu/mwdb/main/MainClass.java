package edu.asu.mwdb.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.asu.mwdb.beans.GestureOneDim;
import edu.asu.mwdb.beans.OneDGestureWords;
import edu.asu.mwdb.beans.SensorWord;
import edu.asu.mwdb.exceptions.InvalidPathException;
import edu.asu.mwdb.fileio.LoopThroughAllFiles;
import edu.asu.mwdb.operations.TFIDFUtils;
import edu.asu.mwdb.util.ComparisionUtils;
import edu.asu.mwdb.util.Constants;
import edu.asu.mwdb.util.DrawImage;
import edu.asu.mwdb.util.GenerateWords;
import edu.asu.mwdb.util.Misc;
import edu.asu.mwdb.util.NormalDistributionUtils;
import edu.asu.mwdb.util.NormalizeGesture;
import edu.asu.mwdb.util.Validators;

/**
 * This is the main class from which control is operated
 * @author kishanmaddula
 *
 */
public class MainClass {
	public static void main(String args[]){
		try{
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Use Defaults?");
			String option = buf.readLine();
			String inputPath="";
			float stdDev,mean;
			int window=0,shift=0,resolution;
			//Default configuration parameters
			if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
				//inputPath = "/Users/kishanmaddula/Dropbox/Assignments/MWDB/Phase 1/sampledata/X";
				inputPath = "/Users/kishanmaddula/Desktop/data set";
				stdDev = 0.25f;
				mean = 0;
				window = 5;
				shift = 3;
				resolution = 5;
			}
			//User selected parameters
			else{
				System.out.println("Please enter the input folder: ");
				inputPath = buf.readLine();
				if(!Validators.validatePath(inputPath))
					throw new InvalidPathException(inputPath);
				System.out.println("Standard Deviation: ");
				stdDev = Float.parseFloat(buf.readLine());
				System.out.println("Mean: ");
				mean = Float.parseFloat(buf.readLine());
				System.out.println("Window Length: ");
				window = Integer.parseInt(buf.readLine());
				System.out.println("Shift Distance: ");
				shift = Integer.parseInt(buf.readLine());
				System.out.println("Resolution: ");
				resolution = Integer.parseInt(buf.readLine());
			}
			
			//Set the global parameters to be used in other files
			Constants.WINDOW_LENGTH = window;
			Constants.SHIFT_LENGTH = shift;
			NormalDistributionUtils.MEAN = mean;
			NormalDistributionUtils.STD = stdDev;
			NormalDistributionUtils.RESOLUTION = resolution;

			// Loop through all the files in the input path and get gesture data
			LoopThroughAllFiles loop = new LoopThroughAllFiles();
			HashMap<String, GestureOneDim> gestures = loop.getAllGestures(inputPath);
			
			// Initialize various data objects
			ArrayList<GestureOneDim> normalizedGestures = new ArrayList<GestureOneDim>();
			HashMap<String,OneDGestureWords> fileWords = new HashMap<String,OneDGestureWords>();
			HashMap<String, GestureOneDim> normalGestures = new HashMap<String, GestureOneDim>();
			ArrayList<String> fileNames = new ArrayList<String>();
			Iterator iterator = gestures.entrySet().iterator();
			
			//Get current time
			Calendar cal = Calendar.getInstance();
			long befTime = cal.getTimeInMillis();

			// Iterate through each gesture, normalize, generate words and generate TF values
			while(iterator.hasNext()){
				Map.Entry pairs = (Map.Entry) iterator.next();
				System.out.println("Gesture " + pairs.getKey().toString());
				GestureOneDim normalized = NormalizeGesture.normalize((GestureOneDim)pairs.getValue());
				normalGestures.put(pairs.getKey().toString(), normalized);
				normalizedGestures.add(normalized);
				GenerateWords words = new GenerateWords(window, shift);
				OneDGestureWords actualWords = words.generateWords(normalized);
				fileWords.put(Misc.getFileName(pairs.getKey().toString()),actualWords);
				ArrayList<HashMap<String,Float>> results =TFIDFUtils.calculateTFValues(actualWords,Integer.parseInt(Misc.getOnlyFileName(pairs.getKey().toString())),Misc.getOnlyFileName(pairs.getKey().toString()));
				TFIDFUtils.printTFValues(results);
				fileNames.add(Misc.getOnlyFileName(pairs.getKey().toString()));
			}

			//Calculate IDF, IDF2, TFIDF, TFIDF2
			TFIDFUtils.calculateIDF();
			TFIDFUtils.calculateIDF2();
			TFIDFUtils.calculateTFIDF();
			TFIDFUtils.calculateTFIDF2();
			TFIDFUtils.printIDF2();
			TFIDFUtils.printIDFValues();
			
			Calendar cal2 = Calendar.getInstance();
			long aftTime = cal2.getTimeInMillis();
			
			System.out.println("Total time taken to calculate TF, IDF, IDF2 (in secs): " + (float)(aftTime - befTime)/1000 );
			
			//Check if user needs to generate heatmap
			option = "Y";
			while(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
				System.out.println("Do you want to enter number of input gesture file?");
				option = buf.readLine();
				if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					System.out.println("File number please");
					String fileNum = buf.readLine();
					OneDGestureWords selected = fileWords.get(fileNum);
					if(selected==null){
						System.out.println("File not found");
					}
					else{
						System.out.println("Enter your top 10 choice:");
						System.out.println("\n1. TF\n2. IDF\n3. IDF2\n4. TF-IDF\n5. TF-IDF2");
						int choice = Integer.parseInt(buf.readLine());
						ArrayList<SensorWord> topWords = TFIDFUtils.getTopWords(selected,choice,Misc.getOnlyFileName(fileNum));
						//TFIDFUtils.printTFValues(fileNum);
						DrawImage.drawImage(normalGestures.get(fileNum),selected,topWords);
					}
				}
			}

			// Check if user wants to get similar vectors
			option = "Y";
			while(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
				System.out.println("Do you want to enter data file?");
				option = buf.readLine();
				if(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("yes")){
					System.out.println("Please enter the input file: ");
					inputPath = buf.readLine();
					GestureOneDim inputFile = new LoopThroughAllFiles().readSingleFile(inputPath);
					System.out.println("How do you want to compare?");
					System.out.println("\n1. TF\n2. TF-IDF\n3. TF-IDF2");
					int compareOption = Integer.parseInt(buf.readLine());
					ArrayList<String> similarFiles = new ArrayList<String>();
					switch(compareOption){
					case 1 : similarFiles = ComparisionUtils.getSimilarTFDocs(inputFile,fileNames);
							 break;
					case 2 : similarFiles = ComparisionUtils.getSimilarTFIDFDocs(inputFile, fileNames);
							 break;
					case 3 : similarFiles = ComparisionUtils.getSimilarTFIDF2Docs(inputFile, fileNames);
							 break;
					}
					System.out.println("Similar Files:");
					for(int i=0;i<similarFiles.size();i++){
						System.out.print(similarFiles.get(i)+"\t");
					}
					System.out.println();
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
			e.printStackTrace();
		}
	}
}
