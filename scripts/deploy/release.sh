#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path/../..;pwd`
project=$root/stan
release=$1
todeploy=$root/todeploy/$release

$root/scripts/build.sh

echo "******* Preparing Deploy Package $release *******"
rm -rf $todeploy
mkdir -p $todeploy
cp $project/stan $todeploy
cp $project/stan.jar $todeploy
cp -r $project/templates $todeploy
cp -r $project/assets $todeploy
cp $project/log4j.properties $todeploy
cp $project/*.sh $todeploy
echo $release "$(date)" > $todeploy/version
chmod 775 -R $todeploy
echo "******* Done *******"

