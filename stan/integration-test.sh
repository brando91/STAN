#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

signal "Running Integration Tests"
java -cp .:'stan.jar' org.junit.runner.JUnitCore integration.IntegrationTestSuite
