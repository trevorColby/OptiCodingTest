# Makefile for 'querier' module                                                                                                                                                                             
#trevor Colby                                                                                                                                                                                               
# May 9, 2017                                                                                                                                                                                            

L = ../libcs50

CC=gcc
CFLAGS=-Wall -pedantic -std=c11 -ggdb -I$L
PROG = querier
OBJS = querier.o ../common/readlinep.o 
LIBS = -lm
LLIBS = $L/libcs50.a-Linux
LDLIBS = -lcurl

                                                                                                                                                

.PHONY: all clean

all: $(PROG)

# executable depends on object files                                                                                                                                                                        
$(PROG): $(OBJS) $(LLIBS)
	$(CC) $(CFLAGS) $(OBJS) $(LLIBS) $(LIBS) $(LDLIBS) -o $(PROG)

# object files depend on include files                                                                                                                                                                      
indextest.o: $L/hashtable.h $L/set.h ../common/readlinep.h $L/memory.h $L/webpage.h $L/webpage_internal.h $L/bag.h
	$(CC) $(CFLAGS) -c querier.c

common.a:
	../common/common.a: $querier.o
		ar rcs $@ $^


clean:
	rm -f $(PROG)
	rm -f *~ *.o
	rm -rf *.dSYM


	