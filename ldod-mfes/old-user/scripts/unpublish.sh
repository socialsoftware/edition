#!/bin/bash

curl -F id=$1 -F name=$1 http://localhost:8080/unpublish