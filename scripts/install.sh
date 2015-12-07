#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path/../;pwd`
cd $root

service=stan
code=$root/$service

echo "******* Installing STAN *******"
sudo cp $code/$service /etc/init.d
sudo update-rc.d $service defaults
sudo service $service stop
./scripts/build.sh
sudo chmod -R 777 $code
sudo service $service start
$code/production-test.sh
echo "******* Done *******"
