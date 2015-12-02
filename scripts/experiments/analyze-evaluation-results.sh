#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root/../../stan
java -cp .:'stan.jar' it.disco.unimib.labeller.tools.AnalizeEvaluationResults $@
