#! /bin/sh
dirs='store utils router vanilla-jsx'

rm -r dist

for dir in $dirs
do
  cd $dir
  yarn test
  cd ..
done