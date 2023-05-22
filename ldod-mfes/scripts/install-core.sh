#! /bin/sh

dirs="about user reading social text virtual search"


for dir in $dirs
do
  cd $dir
  yarn unlink shared
  yarn link shared
  cd ..
done

sh scripts/publish-all-dev.sh
