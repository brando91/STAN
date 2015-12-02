#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../;pwd`
project=$root/stan
build=$project/build

cd $root/evaluation

signal "Setting Up Environment"
sudo apt-get install python python-pip bzip2 p7zip-full
sudo pip install rdflib

if ! command -v trec_eval ; then
	cd tools
	wget http://trec.nist.gov/trec_eval/trec_eval_latest.tar.gz
	tar xvzf trec_eval_latest.tar.gz
	cd trec_eval.9.0
	make
	sudo make install
	make quicktest
	cd ..
	rm trec_eval_latest.tar.gz	
	cd ..
fi

if [ ! -e tools/luke_5.3.0/target ]; then
	signal "Installing Luke 5.3.0"
	cd tools/luke_5.3.0
	mvn clean install
fi
signal "Done"

signal "Building Project"
cd $project
rm -rf $build
mkdir -p $build
javac -encoding utf8 -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
for file in $(find ../lib/* | grep .jar)
do
	jar xf $file
done

cp -r META-INF/services $build
rm -r META-INF/*
cp -r services META-INF/
rm -r services

cd $build
jar cvfe ../stan.jar -C . > /dev/null
chmod 777 ../stan.jar
signal "Done"

