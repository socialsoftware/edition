#! /usr/bin/zsh

dirs='store utils router vanilla-jsx'

rm -r public

for dir in $dirs
do
  cd $dir
  yarn test
  cd ..
done