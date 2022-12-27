#! /bin/sh

dirs="store utils ldod-events router vanilla-jsx fetcher modal tooltip table buttons bootstrap"

rm -r dist
mkdir dist
yarn
cp -rf node_modules dist

for dir in $dirs
do
  cd $dir
  yarn install
  yarn build
  cd ..
done
