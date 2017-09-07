### Trevor Colby
### CS50 Professor David Kotz
### 5/08/2017

# TESTING

### For basic testing run the premade testing script querierTesting.sh
### For additional testing run the fuzzquery provided by Professor Kotz (run by ./fuzzquery)


This test checks the following:

Validity of Directory

If file will open succesfully

Wrong number of args

Too few args

Too many args

### For Results Basic Output see below

[trevor33@torsion ~/cs50/labs/tse/querier]$ querierTesting.sh
These directories and files may not exist so please adjust accordingly

Querier Test invalid file
Exit status 4: invalid file


Querier Test invalid directory
File opened succesfully
Parsing completed succesfully
Query: hellp
No documents match


Querier Test No arguments
Exit status 1: No arguments
Usage: querier pagedirectory indexFile


Querier Test too many arguments
Exit status 2: wrong number of arguments


### With Valid Inputs

[trevor33@torsion ~/cs50/labs/tse/querier]$ querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile
File opened succesfully
Parsing completed succesfully
Query: the
Matches 3 documents (ranked): 
score     7 doc    2: http://old-www.cs.dartmouth.edu/~dfk/
score     4 doc    3: http://old-www.cs.dartmouth.edu/~dfk/visit-dartmouth.html
score     4 doc    1: http://old-www.cs.dartmouth.edu/~cs50/index.html


[trevor33@torsion ~/cs50/labs/tse/querier]$ querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile
File opened succesfully
Parsing completed succesfully
Query: the or professor
Matches 3 documents (ranked): 
score     9 doc    2: http://old-www.cs.dartmouth.edu/~dfk/
score     4 doc    3: http://old-www.cs.dartmouth.edu/~dfk/visit-dartmouth.html
score     4 doc    1: http://old-www.cs.dartmouth.edu/~cs50/index.html


[trevor33@torsion ~/cs50/labs/tse/querier]$ querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile
File opened succesfully
Parsing completed succesfully
Query: the and professor or code and member
Matches 1 documents (ranked): 
score     2 doc    2: http://old-www.cs.dartmouth.edu/~dfk/




#### fuzzquery.c provides much higher quality checking


