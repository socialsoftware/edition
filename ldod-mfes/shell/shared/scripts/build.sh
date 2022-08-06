#! /bin/sh

dirs="store utils router vanilla-jsx fetcher modal tooltip"

rm -r dist

for dir in $dirs
do
  cd $dir
  yarn install
  yarn build
  cd ..
done