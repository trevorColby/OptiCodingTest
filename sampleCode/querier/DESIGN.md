### Trevor Colby
### CS50 Professor David Kotz
### 5/08/2017

## Querier:
### A program to bring together the crawler and indexer lab projects to create a tiny command line run search engine

#### User Interface
Usage: ./querier pageDirectory indexFilename
example: ./querier /net/server/user/cs50/writeTo/pageCollection /net/server/user/cs50/writeTo/indexFile

#### Inputs and Outputs
Inputs: Command line interface is only input (user interface above)
Outputs: Assuming proper user input syntax: (if not then it will return error describing their mistake)

Prints out number of documents matching the query. Followed by the the documents ranked by score. The output is in the format of: score, doc ID, url
Example Query: (Credit: David Kotz)

		>Query: dartmouth or computer science or programming or unix or doug mcilroy 
		>Matches 7 documents (ranked):
		>score 292 doc   7: http://old-www.cs.dartmouth.edu/~dfk/papers/index.html
		>score   9 doc   4: http://old-www.cs.dartmouth.edu/~dfk/postdoc.html
		>score   7 doc   6: http://old-www.cs.dartmouth.edu/~dfk/people.html
		>score   6 doc   1: http://old-www.cs.dartmouth.edu/~dfk/index.html
		>score   5 doc  10: http://old-www.cs.dartmouth.edu/~dfk/ChampionInternationalProfessor.html
		>score   4 doc   9: http://old-www.cs.dartmouth.edu/
		>score   4 doc   5: http://old-www.cs.dartmouth.edu/~dfk/teaching.html
Example Improper Syntax Return: (Credit: David Kotz)

		>Query: and 
		>Error: 'and' cannot be first
		>Query: or 
		>Error: 'or' cannot be first
		>Query: and dartmouth 
		>Error: 'and' cannot be first
		>Query: or dartmouth 
		>Error: 'or' cannot be first
		>Query: dartmouth college or 
		>Error: 'or' cannot be last
		>Query: dartmouth college and 
		>Error: 'and' cannot be last
		>Query: dartmouth college and or computer 
		>Error: 'and' and 'or' cannot be adjacent
		>Query: dartmouth college and and computer 
		>Error: 'and' and 'and' cannot be adjacent
		>Query: dartmouth college or and computer 
		>Error: 'or' and 'and' cannot be adjacent
		>Error: bad character '5' in query.
		>Error: bad character '!' in query.
		>Error: bad character '-' in query.

#### Functional Decompisition into Modules

1.) Main: parses arguments and calls other functions
2.) querier: checks which docs satisfy query and returns them for viewing by the user
3.) Crawler: Sorts through webpage html to collect all connected webpages up to a certain depth
4.) Indexer: Creates a usable index of webpages to be assesed by the querier

Helper Modules:
1.) Hashtable data type
2.) Bag data type
3.) Set data type

####Pseudo code for logic/algorithmic flow

1.) Parse user input and reject invalid input from command line
2.) Allow user to enter query and scan input
3.) Asses query: run through and make sure query itself is in the correct format/valid
4.) Evaluate Documents: take the query entry and scan through webpage index assigning each a score
5.) Return all of the documents that returned a match in descending order in terms of their score
6.) Allow the user to decide what to do next (another query optional)

####Dataflow through modules

1.) Main Parses input and calls querier function
2.) Querier asks user for input and scans in their query
3.) Querier uses indexer developed index to sort through webpages effeciently and assigns each a score based on the user entered query
4.) Given the score of each webpage: Querier retrieves the neccesary webpages from the crawler created directory depending on each's newly assigned score
5.) Returns webpages to user for viewing: descending order in terms of score

####Major data structures

1.) Index of webpages for evaluation by the querier
2.) Crawler created directory of webpages 
3.) Bag for ordering webpages
4.) Array of structures to hold key score pairs 
####Testing Plan

1.) Check command line arguments to insure proper parseing (specifically fringe cases)
2.) Seed Directory to non existing directory
3.) Seed file to non existing file
4.) Incorrect Query (all subsequent improper input)
5.) Check correct inputs run multiple times to check for consistency


