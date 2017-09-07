###Note these directories or files may not exist where you are running them, please adjust accordingly
echo 
echo
echo These directories and files may not exist so please adjust accordingly
echo
echo Querier Test invalid file

querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/aldkfja;
echo

echo Querier Test invalid directory 
querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/aafdsjkfj /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile
echo

echo Querier Test No arguments
querier
echo

echo Querier Test too many arguments
querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile wow
echo

echo Querier Test valid

querier /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexDir /net/tahoe3/trevor33/cs50/labs/tse/writeTo/indexFile
echo
