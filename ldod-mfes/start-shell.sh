#! /bin/bash

export NODE_OPTIONS=--openssl-legacy-provider

rm -rf shell/server/static/*

yarn --cwd ../ldod-visual run build
yarn --cwd ../classification-game run build

yarn --cwd shell/shared install
yarn --cwd shell/shared run publish
  
yarn --cwd shell/client install
yarn --cwd shell/client run publish

docker-compose build
docker-compose up -d