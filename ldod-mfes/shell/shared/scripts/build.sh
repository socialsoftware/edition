#! /bin/sh

dirs="store utils router vanilla-jsx fetcher"

rm -r dist

for dir in $dirs
do
  cd $dir
  yarn install
  yarn build
  cd ..
done