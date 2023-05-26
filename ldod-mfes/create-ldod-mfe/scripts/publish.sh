#!/bin/bash
res=$(curl -F id=$1 -F name=$1 -F entry=$2 -F file=$3  $4/publish)
echo $res