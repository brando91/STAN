#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../;pwd`
version=$1
service=stan
todeploy=$root/todeploy/$version
code=/opt/$service

signal "Installing $version"
ls -d $code/* | grep -v -e logs -e working-area | sudo xargs rm -rf
sudo mkdir -p $code/logs
sudo mkdir -p $code/working-area
sudo cp -R $todeploy/*  $code/
sudo chmod -R 777 $code
sudo mv $code/$service /etc/init.d
sudo update-rc.d $service defaults
sudo service $service stop
sudo service $service start
sudo $code/scripts/production-test.sh
signal "Done"
