#! /usr/bin/zsh

dirs=('shell/shared' 'shell/client')

for dir in "${dirs[@]}"
do
  cd $dir
  yarn install
  yarn run publish
  cd ../..
done

dirs=('home' 'user')

for dir in "${dirs[@]}"
do
  cd $dir
  yarn install
  yarn run publish
  cd ..
done