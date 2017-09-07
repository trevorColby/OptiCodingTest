Trevor Colby
8/29/2017
Opti Coding Evaluation

Storm Event Tracker DESIGN
---------------------------

#### Overview
- Main
	- Description: 
		- Takes a parameter .json file from the command line
		- Contains simple loop to walk through entire .json file

#### Specifics
- Function prototypes and object declaration
	- _Object_
		- probeReading
			- a object to keep track of the data of each storm event
			- contains info from 4 sensors as well as time
			- represents *instantaneous* reading from probe
	- _FUNCTIONS_
		1. Read in data from .json file
			- used by loop to walk through all of .json file
		2. Fill object with read data
			- function to construct and return object
		3. Place object into array of objects
			- will represent the recording of all data
		4. Print out object data
			- will print out to screen and file
		5. Calculate 15 minute change
			- uses past three data entries to calc


