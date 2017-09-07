/*
Trevor Colby
8/29/2017
Opti Coding Test
Storm Event Class: class used to define the storm event object and its
subsequent constructors and functions
*/

public class probeReading {
	public int number=1;
	public String time ="No Time";
	public double rainIn =0;
    public double nCSoilMoisture =0;
    public double sCSoilMoisture =0;
    public double nSoilMoisture =0;
    public double sSoilMoisture =0;
    public double rainChange15 =0;
    public double ncSoilChange=0;
    public double scSoilChange=0;
    public double nSoilChange=0;
    public double sSoilChange=0;

    //default constructor
    public probeReading() {
    	number=1;
        time ="No Time";
		rainIn =0;
    	nCSoilMoisture =0;
    	sCSoilMoisture =0;
    	nSoilMoisture =0;
    	sSoilMoisture =0;
    	rainChange15=0;
    	ncSoilChange=0;
    	scSoilChange=0;
    	nSoilChange=0;
    	sSoilChange=0;
    }

    //primary constructor to set data members
    public probeReading
    (int numSet,String timeSet, double rainSet, double ncsmSet, double scsmSet,double nsmSet, double ssmSet, double rc15Set) {
    	number=numSet;
        time =timeSet;
		rainIn =rainSet;
    	nCSoilMoisture =ncsmSet;
    	sCSoilMoisture =scsmSet;
    	nSoilMoisture =nsmSet;
    	sSoilMoisture =ssmSet;
    	rainChange15=rc15Set;
      	ncSoilChange=0;
      	scSoilChange=0;
    	nSoilChange=0;
    	sSoilChange=0;
    }
    
    //constructor without past 15 min rain change value
    public probeReading(int numSet,String timeSet, double rainSet, double ncsmSet, double scsmSet,double nsmSet, double ssmSet) {
    	number=numSet;
        time =timeSet;
		rainIn =rainSet;
    	nCSoilMoisture =ncsmSet;
    	sCSoilMoisture =scsmSet;
    	nSoilMoisture =nsmSet;
    	sSoilMoisture =ssmSet;
    	rainChange15=0;
    }
    
    //set change in soil moisture values
    public void soilChangeSet(double ncChange,double scChange, double nChange, double sChange){
    	this.ncSoilChange=ncChange;
    	this.scSoilChange=scChange;
    	this.nSoilChange=nChange;
    	this.scSoilChange=sChange;
    }
    
    //method to set rain change value
    //to be leveraged by rain change calculation algorithm
    public void rainChangeSet(double rc15Set){
    	this.rainChange15=rc15Set;
    }
    
    //return the amount of rain the probe recorded
    public double getRainIn(){
    	return this.rainIn;
    }
   
    //function to return the time of the probe reading
    public String getTime(){
    	return this.time;
    }
    
    //function to return rainfall in past 15 minutes
    public double rainFall(){
		return this.rainChange15;
    }
    
    //return 1 if received rain
    //else return 0
    public int gotRain(){
    	if(this.rainIn!=0){
    		return 1;
    	}
    	else return 0;
    }

	//function to print out data reading including all informations
    public void longPrint(){
    	System.out.println("------------STORM EVENT "+ this.number+ " DATA-----------");
    	System.out.println("---------------------------------------");
    	System.out.println("Time of recording: " + this.time);
    	System.out.println("Current Rain Reading: "+ this.rainIn);
    	System.out.println("North Central Soil Moisture: "+ this.nCSoilMoisture);
    	System.out.println("South Central Soil Moisture: "+ this.sCSoilMoisture);
    	System.out.println("North Soil Moisture: "+ this.nCSoilMoisture);
    	System.out.println("Rain Change in past 15 minutes: "+ this.rainChange15);
    	System.out.println("NC Moisture Change: "+ this.ncSoilChange);
    	System.out.println("SC Moisture Change: "+ this.scSoilChange);
    	System.out.println("N Moisture Change: "+ this.nSoilChange);
    	System.out.println("S Moisture Change: "+ this.sSoilChange);
    	System.out.println();
    }
    
    //function to print out only the recording where rain was observed
    public void shortPrint(){
	    System.out.println("------------Probe Reading "+ this.number+ " DATA-----------");
	    System.out.println("Time of recording: " + this.time);
	    System.out.println("Instantaneous Rain Reading: "+ this.rainIn);
	    System.out.println("Rain Change in past 15 minutes: "+ this.rainChange15);
	    System.out.println();
    }
    
}