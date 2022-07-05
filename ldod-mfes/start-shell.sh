#! /bin/bash

cd shell/server
rm -rf static/*
cd ../..

dirs=('shell/shared' 'shell/client')

for dir in "${dirs[@]}"
do
  cd $dir
  yarn install
  yarn run publish
  cd ../..
done

docker-compose build
docker-compose up -d