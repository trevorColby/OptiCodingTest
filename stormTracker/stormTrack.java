/*
Trevor Colby
8/29/2017
Opti Coding Test
stormTrack Purpose: Command line application that takes a .json file as its
input. It then reads in data from this .json file and interpret each
reading as a unique storm event with four probe readings, a rain depth,
running chnage in past 15 mins of rain level, and time of reading. This
data will be returned to the user in the terminal as well as in a file.
*/

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class stormTrack extends probeReading{

//main function used primarily to loop through .json file
//Parameters: takes string input from command line representing .json file destination
public static void main(String[] args){
	int exitStatus=argumentParsing(args);
	if(exitStatus!=0){
		System.out.print("Exiting program");
		System.exit(1);
		System.out.println("Failed to exit");
	}
	
	//Create necessary data holders
	ArrayList<ArrayList<probeReading>> storms = new ArrayList<ArrayList<probeReading>>();
	ArrayList<probeReading> allReadings = new ArrayList<probeReading>();
	ArrayList<Double> rainChanges = new ArrayList<Double>();
	ArrayList<ArrayList<Double>> soilChanges = new ArrayList<ArrayList<Double>>();
	
	//read in all data from the file
	allReadings=fileReader(args[0]);
	if(allReadings==null){
		System.out.println("The file was empty");
		System.exit(2);
	}
	//Calculate running 15 minute change in water level
	rainChanges=rainChangCalc(allReadings);
	soilChanges=soilChangCalc(allReadings);
	allReadings=fillRainChange(allReadings,rainChanges);
	allReadings=soilChangeDetect(allReadings,soilChanges);
	storms=stormFinder(allReadings);
	stormPrint(storms);
}

//function to parse the command line arguments given to the application
//only accepts 1 argument ending in .json format
//Parameter: String from command line
//Output: integer indicating success or failure
public static int argumentParsing(String[] args){
	if(args.length!=1){
		System.out.println("Incorrect Number of arguments.");
		System.out.println("Correct Usage: file name with .json extension");
		return 1;
	}
	if(!args[0].endsWith(".json")){
		System.out.println("Incorrect argument format.");
		System.out.println("Correct Usage: file name with .json extension");
		return 2;
	}
	return 0;
}


//function to read data from .json file
//stores reads objects from file and stores them in custom
//probeReading object, then creates arrayList of all storm events
//Parameter: String of file path to .json file holding data
//Output: ArrayList of custom object probe readings
public static ArrayList<probeReading> fileReader(String filePath){
	JSONParser parser = new JSONParser();
	ArrayList<probeReading> allReadings = new ArrayList<probeReading>();
	try { 
	 	JSONArray arrayOfObjects = (JSONArray) parser.parse(new FileReader(filePath));
	 	int counter=1;
	 	Object currentObject;
	 	for (int i= arrayOfObjects.size()-1; 0<=i; i--){
	 		currentObject=arrayOfObjects.get(i);
	 	    JSONObject event = (JSONObject) currentObject;
	 	    //retrieve all info from .json file
	 	    String time = (String) event.get("timeValue_UTC");
	 	    double rainIn = (double) event.get("Rain_in");
	 	    double NCSoilMoisture = (double) event.get("NorthCentralSoilMoisture_m3_per_m3");
	 	    double SCSoilMoisture = (double) event.get("SouthCentralSoilMoisture_m3_per_m3");
	 	    double NSoilMoisture = (double) event.get("SouthCentralSoilMoisture_m3_per_m3");
	 	    double SSoilMoisture = (double) event.get("SouthCentralSoilMoisture_m3_per_m3");
	 	    //place all info into object and add to ArrayList
	 	    probeReading tempEvent= new probeReading(counter,time,rainIn,NCSoilMoisture,SCSoilMoisture,NSoilMoisture,SSoilMoisture);
	 	    allReadings.add(tempEvent);
	 	    counter++;
	 	    
	 	  }
	 	return allReadings;
	 }
	catch(FileNotFoundException e){
		System.out.println("Not a valid file: file not found");
		return null;
    }
    catch (IOException e) {
    	System.out.println("Not a valid file");
    	return null;
    } 
    catch (ParseException e) {
    	System.out.println("Not a valid file");
    	return null;
    }
}



//function to walk through all storm events and calculate the change in water
//level in the past fifteen minutes
//Parameter: ArrayList of probe readings
//Output: ArrayList of rain change in past 15 mins for every reading
public static ArrayList<Double> rainChangCalc(ArrayList<probeReading> probeReadings){
ArrayList<Double> rainChangeArray = new ArrayList<Double>();
int numEvents = probeReadings.size();

for(int i =0; i<numEvents;i++){
	//control edge cases since no recording from 15 minutes previous
	if(i==0){
		rainChangeArray.add(i, (probeReadings.get(0)).getRainIn());
	}
	//edge case
	else if(i==1){
		double firstRainValue=probeReadings.get(0).getRainIn();
		double secondRainValue=probeReadings.get(1).getRainIn();
		rainChangeArray.add(i, firstRainValue+secondRainValue);
	}
	//general case, find difference in water from 15 minutes previous
	else{
		double dubPrevRainValue=probeReadings.get(i-2).getRainIn();
		double prevRainValue=probeReadings.get(i-1).getRainIn();
		double curRainValue=probeReadings.get(i).getRainIn();
		double change=dubPrevRainValue+prevRainValue+curRainValue;
		//round off data in case of unreasonably long values
		change=Math.round (change * 10000.0) / 10000.0;
		rainChangeArray.add(i, change);
	}
}
return rainChangeArray;
}



//function to walk through list of all rain change values and update with change
//in water level which is one of the probeReading's data members
//Parameter: ArrayList of all prove readings and ArrayList of rain change values
//Output: updated array list of probe readings
public static ArrayList<probeReading> fillRainChange(ArrayList<probeReading> probeReadings,ArrayList<Double> rainChangeVals){
	int numVals=rainChangeVals.size();
	for(int i=0; i<numVals; i++){
		probeReadings.get(i).rainChangeSet(rainChangeVals.get(i));
	}
	return probeReadings;
}

//function to walk through all storm events and calculate the change in soil moisture
//Parameter: ArrayList of probe readings
//Output: ArrayList of ArrayLists of soil moisture for all four probes
public static ArrayList<ArrayList<Double>> soilChangCalc(ArrayList<probeReading> probeReadings){
ArrayList<ArrayList<Double>> soilChangeArray = new ArrayList<ArrayList<Double>>();
int numEvents = probeReadings.size();

for(int i =0; i<numEvents;i++){
	ArrayList<Double> fourProbes = new ArrayList<Double>();
	//control edge cases since no recording from 15 minutes previous
	if(i==0){
		for(int j=0;j<4;j++){
			fourProbes.add(0.0);
		}
		soilChangeArray.add(fourProbes);
	}
	//general case, find difference in water from 15 minutes previous
	else{
		double ncPrevMoist=probeReadings.get(i-1).getRainIn();
		double scPrevMoist=probeReadings.get(i-1).getRainIn();
		double nPrevMoist=probeReadings.get(i-1).getRainIn();
		double sPrevMoist=probeReadings.get(i-1).getRainIn();
		
		double ncCurMoist=probeReadings.get(i).getRainIn();
		double scCurMoist=probeReadings.get(i).getRainIn();
		double nCurMoist=probeReadings.get(i).getRainIn();
		double sCurMoist=probeReadings.get(i).getRainIn();
		
		//change in moisture for all probes
		double ncChange=Math.abs(ncCurMoist-ncPrevMoist);
		double scChange=Math.abs(scCurMoist-scPrevMoist);
		double nChange=Math.abs(nCurMoist-nPrevMoist);
		double sChange=Math.abs(sCurMoist-sPrevMoist);
		
		
		//add change values to arraylist, one for each probe
		fourProbes.add(ncChange);
		fourProbes.add(scChange);
		fourProbes.add(nChange);
		fourProbes.add(sChange);
		//add arraylist to arraylist
		soilChangeArray.add(fourProbes);
	}
}
return soilChangeArray;
}

//function to walk through the list of all soil moisture values and update
//change in moisture which is one of the probeReading's data members
//Parameter: ArrayList of all prove readings and ArrayList of moisture change values
//Output: updated array list of probe readings
public static ArrayList<probeReading> soilChangeDetect(ArrayList<probeReading> probeReadings,ArrayList<ArrayList<Double>> soilChangeVals){
	int numVals=soilChangeVals.size();
	for(int i=0; i<numVals; i++){
		double ncChange=soilChangeVals.get(i).get(0);
		double scChange=soilChangeVals.get(i).get(1);
		double nChange=soilChangeVals.get(i).get(2);
		double sChange=soilChangeVals.get(i).get(3);
		probeReadings.get(i).soilChangeSet(ncChange,scChange,nChange,sChange);
	}
	return probeReadings;
}

//function to pick out individual storms
//walks through all data points and groups them in ArrayList of ArrayLists
//waits 25 minutes between stoppage of rain and creating new storm so that
//if a storm has stop and start rain, it will not create several new storm 
//events but rather keep the one storm event all together
//Parameter: ArrayList of probe Readings
//Output: ArrayList of ArrayLists of storms
public static ArrayList<ArrayList<probeReading>> stormFinder(ArrayList<probeReading> allReadings){
ArrayList<ArrayList<probeReading>> storms = new ArrayList<ArrayList<probeReading>>();
boolean insideStorm = false;
int noRainTimer=0;
int stormNum=-1;
int stormSize=0;
boolean firstStorm=true;
for(probeReading currentReading: allReadings){
	if(((!insideStorm)&&stormSize>0)||firstStorm){
		if(currentReading.getRainIn()!=0){
			ArrayList<probeReading> tempStorm = new ArrayList<probeReading>();
			storms.add(tempStorm);
			firstStorm=false;
			stormNum++;
			insideStorm=true;
			storms.get(stormNum).add(currentReading);
			stormSize=1;
		}
	}
	else if(currentReading.getRainIn()==0){
		noRainTimer++;
		storms.get(stormNum).add(currentReading);
		//wait up to a maximum of 25 minutes before declaring their to be no more storm
		if(noRainTimer>5){
			insideStorm=false;
		}
	}
	else if(currentReading.getRainIn()!=0){
		noRainTimer=0;
		stormSize++;
		storms.get(stormNum).add(currentReading);
	}
	
}
return storms;
}


//function that prints out the time at which each soil probe detected the rain
//Parameter: storm event
//output: print to stdout
public static void moistChngPrint(ArrayList<probeReading> storm){
	int ncChange=storm.size()-1;
	int scChange=storm.size()-1;
	int nChange=storm.size()-1;
	int sChange=storm.size()-1;
	
	for(int i=0; i<storm.size();i++){
		if(storm.get(i).ncSoilChange>.009){
			ncChange=i;
			i=storm.size()+1;
		}
	}
		
	for(int j=0; j<storm.size();j++){
		if(storm.get(j).scSoilChange>.009){
			scChange=j;
			j=storm.size()+1;
		}	
	}
	for(int k=0; k<storm.size();k++){
		if(storm.get(k).nSoilChange>.009){
			nChange=k;
			k=storm.size()+1;
		}	
	}
	for(int l=0; l<storm.size();l++){
		if(storm.get(l).sSoilChange>.009){
			sChange=l;
			l=storm.size()+1;
		}	
	}
	System.out.println("North Central soil probe first detected the change at: "+ storm.get(ncChange).getTime());
	System.out.println("South Central soil probe first detected the change at: "+ storm.get(scChange).getTime());
	System.out.println("North soil probe first detected the change at: "+ storm.get(nChange).getTime());
	System.out.println("Soil soil probe first detected the change at: "+ storm.get(sChange).getTime());
}

//function to print out the collection of storms 
//Parameter: ArrayList of ArrayList of Storms
//Output: print to stdout
public static void stormPrint(ArrayList<ArrayList<probeReading>> storms){
	if(storms.get(0)!=null){
		int stormNumber =1;
		int storedWaterTimer;
		for(ArrayList<probeReading> currentStorm: storms){
			System.out.println("\n");
			System.out.println("--------------------Storm Number: "+ stormNumber+"--------------------");
			System.out.println("First Rain Reading at: " + currentStorm.get(0).getTime());
			moistChngPrint(currentStorm);
			System.out.println("\n15 minute water level updates: ");
			storedWaterTimer=1;
			stormNumber++;
			for(probeReading currentReading: currentStorm){
				storedWaterTimer++;
				if(storedWaterTimer%3==0){
					//don't print out 15 minute update if time glitch is present
					if(!currentReading.getTime().equals("0001-01-01T00:00:00")){
						System.out.println(currentReading.getTime() +"-> " +currentReading.rainChange15 + " inches" );
					}
				}
			}
		}
	}
}
}
