#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/..;pwd`
cd $root

signal "Running Production Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.stan.production.ProductionTestSuite
