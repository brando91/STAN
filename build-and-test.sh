#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/stan

cd $root

scripts/build.sh

cd $project

signal "Running Algorithm Unit Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.unit.UnitTests
signal "Done"

signal "Running STAN Unit Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.stan.unit.TestSuite
signal "Done"
