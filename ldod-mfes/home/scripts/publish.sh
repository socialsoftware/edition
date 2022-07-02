#!/bin/bash

curl -F id=$1 -F name=$1 -F entry=$2 -F file=$3  http://localhost:8080/publish