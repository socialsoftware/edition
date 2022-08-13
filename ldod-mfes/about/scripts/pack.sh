#!/bin/bash

rm -rf dist
mkdir dist
tar -czvf dist/$1.tgz -C ./build ./