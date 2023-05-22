#! /bin/sh

dirs="event-bus ldod-ui bootstrap ldod-core ldod-core-ui"

rm -r dist
mkdir dist
cp ./scripts/package.json dist

for dir in $dirs
do
  cd $dir
  yarn
  yarn build
  cd ..
done
