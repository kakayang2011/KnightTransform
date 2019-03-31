#!/usr/bin/env bash

echo "begin init project~~"

list="knight-transform knight-tinyImage-plugin knight-doubleCheck-plugin knight-shrinkR-plugin  knight-doubleCheck-library knight-shrinkInline-plugin library"

for state in ${list}
do
    echo "this word is $state"
    sed -i "" "s/\/\/k//g" ${state}/build.gradle
    ./gradlew clean :${state}:uploadArchives --stacktrace 2>&1| tee log.txt
    buildResult=`grep "BUILD FAILED" log.txt`
    if [[ ${buildResult} =~ "BUILD FAILED" ]]
    then
        echo "please check your project"
        rm -f log.txt
        exit 1
    fi
    rm -f log.txt
done

#project build.gradle
#sed -i "" "/15/a tinyImage=false" local.properties
line=`sed -n "$=" local.properties`
sed -i "" "$line a\\
tinyImage=true" local.properties

line=`sed -n "$=" local.properties`
sed -i "" "$line a\\
doubleCheck=true" local.properties

line=`sed -n "$=" local.properties`
sed -i "" "$line a\\
shrinkInline=true" local.properties

line=`sed -n "$=" local.properties`
sed -i "" "$line a\\
shrinkR=true" local.properties
