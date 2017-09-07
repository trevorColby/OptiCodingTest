Trevor Colby
8/29/2017
Opti Coding Evaluation

Storm Event Tracker IMPLEMENTATION
----------------------------------

### General Data Types Used
- Double
	- waterChange15
	- RainIn
	- NCSoilMoisture
	- SCSoilMoisture
	- NSoilMoisture
	- SSoilMoisture
	-NCMoistureChange
	-SCMoistureChange
	-SMoistureChange
	-NMoistureChange
- String
	- Time
- Class
	- stormEvent
- Array
	- Array of data readings
- Array of Array 
	- Array COntaining array of data readings
- Files
	- .json file containing raw data

-
##### stormEvent.java
- Function prototypes and object declarations
	- _Object_
		- Probe Reading
			- waterChange15 (change in water level in past 15 mins)
			- Time (or could has function as read in)
			- RainIn
			- NCSoilMoisture
			- SCSoilMoisture
			- NSoilMoisture
			- SSoilMoisture
	- _FUNCTIONS_
		1. Read in data from .json file
			- Take .json file address as parameter
		2. Fill class with read data
			- Parameters:
				- waterChange15 (to be calculated at runtime)
				- Time (or could has function as read in)
				- RainIn
				- NCSoilMoisture
				- SCSoilMoisture
				- NSoilMoisture
				- SSoilMoisture
			- Returns:
				- Filled Class
		3. Place class into array of classes
			- Parameters
				- Dynamic array to fill
				- Class to place into array
			- Returns:
				- Array of Classes
		4. Print out class data
			- Parameters:
				- Array of classes
				- File to print to (also print to stdout)
			- Returns
				- Null (but prints to stdout and file that it creates)
		5. Calculate 15 minute change
			- Parameters:
				- Counter (integer)
				- Array of Classes
			- Returns:
				- chnage in water storaged in past 15 mins
		6.  Main
			- Task:
				- Contains simple loop to walk through entire .json file
				- wait between each object read in to print out data
			- Parameters:
				- read in .json data file from command line
			- Output: 
				- print table to stdout and generated file



