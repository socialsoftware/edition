#!/bin/bash

res=$(curl -d id=$1 -d name=$1 $2/unpublish)
echo $res