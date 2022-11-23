#! /bin/sh

dirs="home about user reading social text virtual search annotations"


for dir in $dirs
do
  cd $dir
  yarn unpublish
  cd ..
done

