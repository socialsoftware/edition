#! /usr/bin/zsh

dirs=('store' 'utils' 'router' 'vanilla-jsx')

rm -r dist

for dir in "${dirs[@]}"
do
  cd $dir
  yarn install
  yarn build
  cd ..
done