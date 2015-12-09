#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../;pwd`

service=stan
now=$(date +%s)
todeploy=$root/todeploy/$service-$now
code=/opt/$service

cd $root
./scripts/deploy/release.sh $service-$now

echo "******* Installing STAN *******"
ls -d $code/* | grep -v -e logs -e working-area | sudo xargs rm -rf
sudo mkdir -p $code
sudo mkdir -p $code/logs
sudo mkdir -p $code/working-area
sudo cp -R $todeploy/*  $code/
sudo chmod -R 777 $code
sudo mv $code/$service /etc/init.d
update-rc.d $service defaults
sudo service $service stop
sudo service $service start
echo "******* Done *******"
