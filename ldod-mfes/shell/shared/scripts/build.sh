#! /bin/sh

dirs="popper store utils router vanilla-jsx fetcher modal tooltip table buttons bootstrap"

rm -r dist

cp -rf select-pure dist


for dir in $dirs
do
  cd $dir
  yarn install
  yarn build
  cd ..
done

