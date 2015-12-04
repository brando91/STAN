#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
scripts=$root/scripts
infrastructure=$scripts/infrastructure
indexes=$scripts/indexes
kb=$1

signal "Setting Up Environment"
sudo apt-get update
sudo apt-get install python python-pip bzip2 p7zip-full
sudo pip install rdflib
signal "Done"

cd $root

if [[ $kb == dbpedia ]]
then
	$infrastructure/dbpedia-data.sh
	$indexes/build-index-for-dbpedia.sh
	$indexes/scaled-depths-for-dbpedia.sh
fi
if [[ $kb == yago1 ]]
then
	$infrastructure/yago1-data.sh
	$indexes/build-index-for-yago1.sh
	$indexes/scaled-depths-for-yago1.sh
fi
