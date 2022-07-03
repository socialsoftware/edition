#!/bin/bash

curl  -F id=$1 -F name=$1 -F entry=$2 -F file=$3  http://localhost:9000/publish