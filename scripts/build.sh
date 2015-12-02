#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../;pwd`
project=$root/stan
build=$project/build

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

