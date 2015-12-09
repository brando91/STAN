#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../;pwd`
deploy=$root/scripts/deploy
service=stan
now=$(date +%s)

cd $deploy
./release.sh $service-$now
./install-version.sh $service-$now
