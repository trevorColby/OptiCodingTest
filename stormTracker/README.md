Tevor Colby
8/29/2017
Opti Coding Evaluation

Storm Event Tracker Read Me
---------------------------
#### Compile Using:
 - javac -cp json-simple-1.1.1.jar -g stormTrack.java probeReading.java
#### Run Using: 
 - java -cp json-simple-1.1.1.jar:. stormTrack (Followed by: .json file like GreenRoofStorms.json)
#### Cleanup Using: 
 - make clean (please note: makefile was included just for the cleaning process)

#### Overview
- Both .java files are contained with the stormTracker directory (probeReading and stormTrack)
- Within the stormTracker directory is also contained the neccesary jar external library needed to run the program
- Output is as follows: Storm number is given and below is listed the time at which each moisture probe detected a .009 change in moisture
- Within the each storm is listed the change in water level detected every 15 minutes

#### Design Choices Note:
- For my design I chose to allow up to a 25 minute gap between rain readings and have it stil count it as the same storm
- This was designed to take into account stop/start that can happen with rain in storms or lighter rain that would have 
- less than the measurable amount of rain fall in 5 minutes
- The moisture change was detected up to a ".009" change since small fluctuations did occur without rain actually starting

#### Sample Output:



--------------------Storm Number: 1--------------------

- First Rain Reading at: 2015-06-11T17:35:00
- North Central soil probe first detected the change at: 2015-06-11T17:35:00
- South Central soil probe first detected the change at: 2015-06-11T17:35:00
- North soil probe first detected the change at: 2015-06-11T17:35:00
- Soil soil probe first detected the change at: 2015-06-11T21:00:00

15 minute water level updates: 

- 2015-06-11T17:55:00-> 0.08 inches
- 2015-06-11T18:10:00-> 0.03 inches
- 2015-06-11T18:25:00-> 0.02 inches
- 2015-06-11T18:40:00-> 0.01 inches
- 2015-06-11T18:55:00-> 0.02 inches
- 2015-06-11T19:10:00-> 0.03 inches
- 2015-06-11T19:25:00-> 0.01 inches
- 2015-06-11T19:40:00-> 0.01 inches
- 2015-06-11T19:55:00-> 0.07 inches
- 2015-06-11T20:10:00-> 0.05 inches
- 2015-06-11T20:25:00-> 0.0 inches
- 2015-06-11T20:40:00-> 0.01 inches
- 2015-06-11T20:55:00-> 0.0 inches


