# STAN [![Build Status](https://api.travis-ci.org/brando91/STAN.svg?branch=master)](https://travis-ci.org/brando91/STAN)

The web is full of high quality data in the form of tables that could be integrated to generate new knowledge. However web tables are often lacking in semantics. A crucial problem is the correct relationships detection between a table and an ontology in order to define the semantics. In this context STAN is a tool for semi-automatic semantic annotation of tables that lets non technicians carry out the annotation process. 

## Checking out the repository and configuring your local machine
```
#!bash
$ git clone https://github.com/brando91/STAN.git
$ cd STAN
$ ./build-and-test.sh
```

## Create the required indexes
```
#!bash
$ ./build-index.sh dbpedia
$ ./build-index.sh dbpedia
```
