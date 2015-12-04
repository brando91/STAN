#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../;pwd`
tools=$root/tools

signal "Setting Up Environment"
sudo apt-get install python python-pip bzip2 p7zip-full
sudo pip install rdflib

if [ ! -e $tools/luke_5.3.0/target ]; then
	signal "Installing Luke 5.3.0"
	cd $tools/luke_5.3.0
	mvn clean install
fi
signal "Done"
