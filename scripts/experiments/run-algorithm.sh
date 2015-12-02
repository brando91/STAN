#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
../build.sh > /dev/null

cd ../../stan
time java -Xms256m -Xmx4000m -cp .:'stan.jar' it.disco.unimib.labeller.tools.RunEvaluation $@

