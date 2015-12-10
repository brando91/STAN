#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../../;pwd`
evaluation=$root/evaluation

cd $relative_path

rm -rf $evaluation/labeller-indexes/dbpedia/depths

./scaled-depths.sh \
taxonomy=$evaluation/dbpedia-category-tree/category-tree.nt \
taxonomy=$evaluation/dbpedia-type-tree/type-tree.nt \
types=$evaluation/types/dbpedia-types \
destination=$evaluation/labeller-indexes/dbpedia/depths

cd $evaluation/labeller-indexes/dbpedia/depths
cat * >> types.csv
ls * | grep -v types.csv | xargs rm -rf

