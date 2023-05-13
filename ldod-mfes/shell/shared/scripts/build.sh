#! /bin/sh

dirs="ldod-ui bootstrap ldod-core ldod-core-ui"

rm -r dist
mkdir dist

for dir in $dirs
do
  cd $dir
  yarn
  yarn build
  cd ..
done
