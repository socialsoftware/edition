#! /bin/sh

dirs="event-bus store utils ldod-events router vanilla-jsx fetcher modal tooltip table buttons bootstrap icons navigation-panel notifications"

rm -r dist
mkdir dist
yarn

for dir in $dirs
do
  cd $dir
  yarn install
  yarn build
  cd ..
done
