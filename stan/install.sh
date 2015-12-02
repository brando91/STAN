#!/bin/bash
set -e
relative_path=`dirname $0`
todeploy=`cd $relative_path;pwd`
cd $todeploy
environment=$1

service=stan
code=/opt/$service

echo "******* Installing version $(cat $todeploy/version) *******"
ls -d $code/* | grep -v -e logs -e working-area -e indexes | xargs rm -rf
mkdir -p $code
mkdir -p $code/logs
mkdir -p $code/working-area
echo $environment > $code/environment
cp -R $todeploy/*  $code/
sudo chmod -R 777 $code
sudo mv $code/$service /etc/init.d
sudo update-rc.d $service defaults
sudo service $service stop
sudo service $service start
$code/$environment-test.sh
echo "******* Done *******"
