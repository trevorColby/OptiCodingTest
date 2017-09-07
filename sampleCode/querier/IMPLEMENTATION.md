### Trevor Colby
### CS50 Professor David Kotz
### 5/08/2017

### Implementation

We implement this querier as a command line driven program that utilizes the crawler and indexer labs.  The *querier* itself doesn't call the  `indexer` or the `crawler` but utilizes their output directories or files. It's main job is to sort through the webpage index and assign each webpage a score based upon the user entered query.

Each query involves sorting through the directory of webpages and then returning the webpages in descending order based upon their assigned score
The Scores are tracked using counters, the counters data type is updated while walking through each query.

To enter a query the user provides a crawler created directory of webpages and a indexer created index of webpages

The `query` itself has two basic operators `and` as well as `or`: _and_ indicates an intersection whereas _or_ indicates a union

To print out the results from each query we put all of the webpages into a keyScore struct and then into a bag to pull them from within the iterator. We then pull them out and place each into an array. We keep track of the size of this array for printing later. We then sort the array with a bubble sort in descending score order. Finally we walkthrough the newly sorted array and print out the results.


### Assumptions

The given directory created by the crawler must be in the proper crawler format

The given index created by the indexer must be in the proper idnexer format

The query will not exceed the buffer (512 character limit) size the static size of the arrays limits this



### Compilation

To compile, simply `make clean` followed by `make`.

To test run the `qeurierTest.sh` script
