#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/stan

signal "STAN server is listening on http://localhost:8081/"
cd $project
java -cp stan.jar it.disco.unimib.stan.main.StartWebApplication &> /dev/null
