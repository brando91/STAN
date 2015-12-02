#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/stan

cd $root

signal 'Updating the Repository'
git pull
signal 'Done'

scripts/build.sh

cd $project

signal "Running Algorithm Unit Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.unit.UnitTests
signal "Done"

signal "Running STAN Unit Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.stan.unit.TestSuite
signal "Done"

if [[ $1 != -skip-regression-tests ]]
then
	signal "Running Regression Tests for DBPedia"
	java -Xms256m -Xmx3000m -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.regression.DBPediaRegressionTest
	signal "Done"

	signal "Running Regression Tests for YAGO1"
	java -Xms256m -Xmx3000m -cp .:'stan.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.regression.YAGO1RegressionTest
	signal "Done"
fi
