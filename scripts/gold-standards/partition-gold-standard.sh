#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

./build.sh > /dev/null

cd $root/../../stan
java -cp .:'stan.jar' it.disco.unimib.labeller.tools.PartitionGoldStandard $@
