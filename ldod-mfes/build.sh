#! /usr/bin/zsh

dirs=('user' 'home' 'about' 'shared')

for dir in "${dirs[@]}"
do
  cd $dir
  yarn install
  yarn build
  cd ..
done
