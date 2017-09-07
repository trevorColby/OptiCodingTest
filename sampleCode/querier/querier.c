//Trevor Colby May 2017
//COSC 50
//PROFESSOR DAVID KOTZ
//querier.c: program written to utilize the indexer.c and crawler.c functionality and the subsequent 
//			 file/pagedirectory creation for sorting user entered query
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "hashtable.h"
#include "counters.h"
#include <dirent.h>
#include <unistd.h>
#include <ctype.h>
#include "set.h"
#include "../libcs50/bag.h"
#include "../libcs50/webpage.h"
#include "../common/readlinep.h"

//strucutre to hold two counters: used in iterator
typedef struct counterStruct {
  counters_t* counter1;	
  counters_t* counter2;     
} counterStruct_t;

//structure to hold key and score pair: for iterator
typedef struct keyScore{
	int key;
	int score;
} keyScore_t;


//Complete list of function prototypes: see below for descriptions
int argsParse(int argc, char *argv[],char** pageDir, FILE** indexFile);
int incrementCounters(hashtable_t *index,char* word, int current);
hashtable_t* scanIndexFile(FILE* indexFile, hashtable_t* index);
char* promptUserInput();
bool tokenizer(char* line, char tokens[512][512]);
bool cleanQuery(char tokens[512][512]);
void zero(char* zeroes);
void doubleZero(char zeros[512][512]);
int doubleCountEntries(char tokens[512][512]);
char* makeLower(char *string);
counterStruct_t* counterStruct_new(counters_t* counterA, counters_t* counterB);
keyScore_t* keyScore_new(int keyA, int scoreB);
void countersCompare(void *arg, const int key, int count);
void countersCombine(void *arg, const int key, int count);
counters_t* andCase(counters_t* counter1, counters_t* counters2);
counters_t* orCase(counters_t* counter1, counters_t* counters2);
counters_t* queryLogic(hashtable_t* index,char tokens[512][512]);
int intLength(int input);
int counterCountEntries(counters_t* queryCounter);
void counterCountIterate(void *arg,const int key, int count);
void countersKeyRetrieve(void *arg,const int key, int count);
keyScore_t** buildArrayFromCounter(counters_t* queryCounter,int* length);
void sortArrayOfStructs(keyScore_t** arrayOfStructs,int length);
void printArrayOfStructs(keyScore_t** arrayOfStructs,char* pageDir,int length);
void keyScore_delete(void* item);


//main function just used to loop anad call other functions
int main(int argc, char *argv[]){
	//declare variables
	char* pageDir;
	FILE* indexFile;
	hashtable_t* index= hashtable_new(512);
	char tokens[512][512];
	char* line;
	//zero(line);
	doubleZero(tokens);
	//continue only if args parsed
	if(argsParse(argc,argv,&pageDir,&indexFile)==0){

		//get data from file read to use and fill index
		scanIndexFile(indexFile,index);
		fclose(indexFile);
		//initial user input prompt
		line=promptUserInput();
		//loop for valid input
		while(!(tokenizer(line,tokens)&&cleanQuery(tokens))){
			//zero(line);
			doubleZero(tokens);
			line=promptUserInput();
		}
		free(line);
		//create counter to house score result from query
		counters_t* final=queryLogic(index,tokens);
		free(index); //kept getting seg fault when attempting hashtable delete function
		//count how many pages there are that are
		int numEntries=counterCountEntries(final);

		//print out Resulting webpages
		if(numEntries==0){
			printf("No documents match\n");
		}
		else{
			printf("Matches %d documents (ranked): \n",numEntries);
		}
		int length=0;
		keyScore_t** arrayForOrdering=buildArrayFromCounter(final,&length);
		free(final);
		//function to sort webpages
		sortArrayOfStructs(arrayForOrdering,length);

		//print out final result in descending order
		printArrayOfStructs(arrayForOrdering,pageDir,length);
		free(arrayForOrdering);
	}
	printf("\n");
	//return upon success
	return 0;
}

//*****LOCAL FUNCTIONS BELOW******//
//********************************//

//fuinction to print out array of structures, uses page direcotry to get url for page, length to loop
void printArrayOfStructs(keyScore_t** arrayOfStructs,char* pageDir,int length){
	int count;
	int key;
	for(int i=0; i<length;i++){
		key=arrayOfStructs[i]->key;
		count=arrayOfStructs[i]->score;
		char* html;
		if(count>0){
			char *fileName = malloc(sizeof(char)*(strlen(pageDir)+intLength(key)));
			sprintf(fileName,"%s/%d",pageDir,key);
			FILE* webPage= fopen(fileName,"r"); //open file for reading
			html=freadlinep(webPage);
			printf("score %5d doc %4d: %s\n",count,key,html);
			free(fileName);
			fclose(webPage);
			free(html);
		}
	}
}

//function to bubble sort an array of structures into descending order
void sortArrayOfStructs(keyScore_t** arrayOfStructs,int length){
	int n=length;
	keyScore_t* swap;
	for (int c=0; c<(n-1); c++){
   		for (int d=0; d<n-c-1; d++){
      		if (arrayOfStructs[d]->score < arrayOfStructs[d+1]->score){
       			swap= arrayOfStructs[d];
        		arrayOfStructs[d]=arrayOfStructs[d+1];
       			arrayOfStructs[d+1] = swap;
      		}
    	}
  	}
}

//function to build array from counter, give it a int place =0 and will update (by reference) to size of array
keyScore_t** buildArrayFromCounter(counters_t* queryCounter,int* place){
	bag_t* bagOfStructs=bag_new();
	keyScore_t* tempKeyScore;
	keyScore_t** arrayOfStructs=malloc(100*sizeof(keyScore_t));
	int currentSize=100;
	counters_iterate(queryCounter,bagOfStructs,countersKeyRetrieve);
	while((tempKeyScore=bag_extract(bagOfStructs))!=NULL){
		if(*place>=currentSize){
			arrayOfStructs=realloc(arrayOfStructs,currentSize*2);
			currentSize=currentSize*2;
		}
		arrayOfStructs[*place]=tempKeyScore;
		(*place)++;
	}
	bag_delete(bagOfStructs,keyScore_delete); //here is the bag delete
	return arrayOfStructs;
}

//function to be handed to interator, fills array arg with key values
void countersKeyRetrieve(void *arg,const int key, int count){
	keyScore_t* keyIntoBag=keyScore_new(key,count);
	bag_insert((bag_t*)arg,keyIntoBag);
}

//iterator function call to count number of entries within a counter
int counterCountEntries(counters_t* queryCounter){
	int entries=0;
	counters_iterate(queryCounter,&entries,counterCountIterate);
	return entries;
}

//function to be used by counter_iteate takes int in arg to keep track of number of entries
void counterCountIterate(void *arg,const int key, int count){
	if(count>0){
		(*(int*)arg)++;
	}
}

//walk through the tokenized query and call the appropriate and/or case function and accumulate result
counters_t* queryLogic(hashtable_t* index,char tokens[512][512]){

	//int orPresent=0;
	counters_t* andCounters1=counters_new();
	counters_t* andAccumulator=counters_new();
	counters_t* totalAccumulator=counters_new();

	//set accumulator
	andAccumulator=hashtable_find(index,tokens[0]);

	for(int i=1; i<doubleCountEntries(tokens);i++){ 
		if(strcmp(tokens[i],"or")!=0){
			if(strcmp(tokens[i],"and")!=0){
				andCounters1=hashtable_find(index,tokens[i]);
				andAccumulator=andCase(andAccumulator,andCounters1);
			}
		}
		else{
			totalAccumulator=orCase(andAccumulator,totalAccumulator);
			andAccumulator=hashtable_find(index,tokens[i+1]);
		}
	}
	totalAccumulator=orCase(andAccumulator,totalAccumulator);
	counters_delete(andCounters1);
	counters_delete(andAccumulator);
	return totalAccumulator;
}

//function to handle cross section of two counters using iterators->may want to remove unused hashtable
counters_t* andCase(counters_t* counters1, counters_t* counters2){
	counterStruct_t* toBeCompared=counterStruct_new(counters1,counters2);
	counters_iterate(counters1,toBeCompared,countersCompare);
	free(toBeCompared);
	return counters1;
}

//function to handle union of two counters using iterators
counters_t* orCase(counters_t* counters1, counters_t* counters2){
	counterStruct_t* toBeCombined=counterStruct_new(counters1,counters2);
	counters_iterate(counters1,toBeCombined,countersCombine);
	free(toBeCombined);
	return counters2;
}

//compare two counters within counters iterator: arg is a countersStruct
void countersCompare(void *arg, const int key, int count){
	if(counters_get( ((counterStruct_t*) arg)->counter2,key)!=0){
		if(counters_get( ((counterStruct_t*) arg)->counter2,key)<count){
			counters_set( ((counterStruct_t*) arg)->counter1,key,counters_get(((counterStruct_t*)arg)->counter2,key));
		}
	}
	else{
		counters_set( ((counterStruct_t*) arg)->counter1,key,0);
	}
}

//combine two counters within counters iterator: arg is a counterStruct
void countersCombine(void *arg, const int key, int count){
	if(counters_get( ((counterStruct_t*) arg)->counter2,key)!=0){
			counters_set( ((counterStruct_t*) arg)->counter2,key,counters_get(((counterStruct_t*)arg)->counter2,key)+count);
			//printf("Counters setting to %d\n",counters_get(((counterStruct_t*)arg)->counter1,key));
	}
	else{
		for(int i=0; i<counters_get( ((counterStruct_t*) arg)->counter1,key); i++){
		counters_add( ((counterStruct_t*) arg)->counter2,key);
		}
	}
}

//create new structure containing two counters
counterStruct_t* counterStruct_new(counters_t* counterA, counters_t* counterB){
  counterStruct_t *twoCounters = malloc(sizeof(counterStruct_t));

  if (twoCounters == NULL) {
    return NULL; // error allocating counterStruct
  } else {
    // initialize contents of counterStruct 
    twoCounters->counter1 = counterA;
    twoCounters->counter2 = counterB;
    return twoCounters;
  }
}

//create new structure containing key and score: must call free
keyScore_t* keyScore_new(int keyA, int scoreB){
  keyScore_t *pair = malloc(sizeof(counterStruct_t));

  if (pair == NULL) {
    return NULL; // error allocating counterStruct
  } else {
    // initialize contents of counterStruct 
    pair->key = keyA;
   	pair->score= scoreB;
    return pair;
  }
}

//scan the index file and build/return an index hashtable from it
hashtable_t* scanIndexFile(FILE* indexFile, hashtable_t* index){
	char word[512];
	int startingpoint;
	int endpoint;
	int key;
	int value;
	char* line;
	if(indexFile!=NULL){
		while((line=freadlinep(indexFile))!=NULL){
			sscanf(line,"%s",word);
			//printf("%s ",word);  //uncomment three print statements if you want to view page output
			startingpoint=strlen(word)+1;
			endpoint=strlen(line);
			for(int i=startingpoint;i<endpoint-1;i+=4){
				key=atoi(&(line[i]));
				value=atoi(&(line[i+1]));
				
				if(hashtable_find(index,word)==NULL){
    				counters_t *occurences = counters_new();
    				hashtable_insert(index,word,occurences);
    				for(int k=0; k<value;k++){
    				incrementCounters(index,word,key);
    			}
    			}
    			else{
    				for(int j=0;j<value;j++){
    					incrementCounters(index,word,key);
    				}
				}
			}
			free(line);
		 }
	}
	return 0;
}

//function to increment a counter within a hashtable
int incrementCounters(hashtable_t *index,char* word, int current){
	counters_add(hashtable_find(index,word),current);
	return 0;
}

//function to parse argument input and add abstraction to main function for clarity
int argsParse(int argc, char *argv[],char** pageDir, FILE** indexFile){
	if(argc == 1){
		printf("Exit status 1: No arguments\n");
		printf("Usage: querier pagedirectory indexFile\n");
		return 1;
	}
	if(argc!=3){
		printf("Exit status 2: wrong number of arguments\n");
		return 2; //exit status 1: wrong number of arguments
	}
	if(argv[1] != NULL){
		DIR* dir = opendir(argv[1]); //check if the pageDir exists and is usable
		if(dir){
			char* crawlerCheck=malloc(sizeof(char)*(strlen(argv[1])+9));
			strcat(crawlerCheck,argv[1]);
			strcat(crawlerCheck,"/.crawler");
			FILE* fp=fopen(crawlerCheck,"r");
			if(fp!=NULL){
				*pageDir=argv[1]; //if directory is valid copy into dir holder
				closedir(dir);
				fclose(fp);
				free(crawlerCheck);
			}
			else{
				printf("Exit status3: invalid directory\n");
				return 3;
			}
		}
		else{
			printf("Exit status 3: invalid directory\n");
			return 3; //exit status 2: invalid directory
		}
	}
	else{
		printf("Exit status 3: invalid directory\n");
		return 3; //exit status 3:invalid directory
	}

	if(argv[2]!= NULL){
		FILE* fp=fopen(argv[2],"r");
		if(fp!=NULL){
			*indexFile=fp;
		}
		else{
			printf("Exit status 4: invalid file\n");
			return 4; //exit status 3: invalid file
		}
	}
	
	else{
		printf("Exit status 4: invalid file\n");
		return 4; //exit status 3: invalid file
	}
	//succesful parse uncomment for confirmation
	//printf("Parsing completed succesfully\n");
	return 0;
}

//split input into usable tokens
bool tokenizer(char* line, char tokens[512][512]){
	int backcount=0; //keep track of head of string
	int tokensCount=0; //keep track of spot in 2d array
	char* validityCheck=calloc(strlen(line),sizeof(char)); //string to hold spots
	int validityLength;
	int length=strlen(line);
	for(int i=0; i<length;i++){
		if(line[i]==' '){ //split at spaces
			strncpy(validityCheck,line+backcount,i-backcount); //filling tokens double array with char arrays
			backcount=i+1;
			strcpy(validityCheck,makeLower(validityCheck));//switch all leters to lower case
			validityLength=strlen(validityCheck);
			for(int k=0;k<validityLength;k++){
				if(!isalpha(validityCheck[k])){ //normalize
					printf("Tokenizer failed: Invalid Input: prohibited character found\n");
					return false;
				}
			}
			if(strcmp(validityCheck," ")!=0){
				if(validityCheck[0]!='\0'){ //eliminate spaces
					strcpy(tokens[tokensCount],validityCheck);
					tokensCount++;
				}
			}
		}
	}
	free(validityCheck);
	if(doubleCountEntries(tokens)==0){
		return false;
	}
	return true;
}

//basic function to call readline and require user input
char* promptUserInput(){
	char* line;
	printf("Query: ");
	line=readlinep();
	strcat(line," ");
	return line;
}

bool cleanQuery(char tokens[512][512]){
	if(strcmp(tokens[0],"and")==0||strcmp(tokens[0],"or")==0){ //check if first word is valid
		printf("Error: first word can contain neither 'and' nor 'or' \n");
				return false;
			}
	for(int j=0; j<doubleCountEntries(tokens);j++){
			if((strcmp(tokens[j],"or")==0 || strcmp(tokens[j],"and")==0) &&(strcmp(tokens[j+1],"or")==0||strcmp(tokens[j+1],"and")==0)){
				printf("Error: adjacent 'and/or' statements \n");
				return false;
			}
			if((j==doubleCountEntries(tokens)-1)&&(strcmp(tokens[j],"or")==0 || strcmp(tokens[j],"and")==0)){
				printf("Error: ending in 'and/or' \n");
				return false;
			}
	}
	return true;
}

//function to walk through a static sized 2d array and clear any memory descrepencies
void doubleZero(char zeroes[512][512]){
	for(int k=0; k<512;k++){
		for(int j=0; j<512;j++){
			strcpy(zeroes[j],"");
		}
	}
}

//function to walk through a static sized array and clear any memory descrepencies
void zero(char zeroes[]){
	for(int f=0; f<512; f++){
		zeroes[f]='\0';
	}
}

//function to count the number of entries in 2d array (array must be zeroed using function before count call)
int doubleCountEntries(char tokens[512][512]){
	int count=0;
		for(int j=0; j<512;j++){
			if(tokens[j][0]!='\0'){
				count++;
		}
	}
	return count;
}

//switch a string to lowercase
char* makeLower(char *string){
	for(int i=0; i<strlen(string);i++){
		string[i]=tolower(string[i]);
	}
	return string;
}

//function to return the length of an integer
int intLength(int input) { 
    if(input>=1000000000) return 10;
    if(input>=100000000) return 9;
    if(input>=10000000) return 8;
    if(input>=1000000) return 7;
    if(input>=100000) return 6;
    if(input>=10000) return 5;
    if(input>=1000) return 4;
    if(input>=100) return 3;
    if(input>=10) return 2;
    return 1;
}

void keyScore_delete(void *item){
	free((keyScore_t*)item);
}

