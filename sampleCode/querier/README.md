## Trevor Colby
### CS50 Professor David Kotz
### 5/08/2017

### OPTI:
-  Please note that this is just a module of a bigger project
- I have included it to demonstrate my ability to work as part of a bigger project
-  It will not compile without the hashtable class which could not be included since it was not my own work

Querier: A program that brings together the functionality of the crawler and indexer to provide a basic search engine service,
it is command line executed

Make: 'make clean' and 'make' calls are all that is neccesary for compiling

Usage: querier pageDirectory indexFilename

pageDirectory: the pathname for an existing directory produced by the Crawler.
indexFilename: the pathname of a readable file.

*Exit Status:*

	0.) Success

	Errors with Inputs: 

	1.):No arguments
	2.):Wrong number of arguments
	3.):Invalid Directory
	4.):Invalid File

	Other Errors

	1.)Error: printed out when invalid query
	2.)No documents math. Printed out when no documents match your query
	
## Limitations:

The query size is liminted to the buffer length and reading in more than the standard length will cause a crash

