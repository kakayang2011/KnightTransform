#!/usr/bin/env bash

echo "begin init project~~"

list="knight-byteK knight-doubleCheck-plugin knight-shrinkR-plugin knight-config-plugin app"

commentStr="classpath \"com.knight"
sed -i "" "s/${commentStr}/\/\/k ${commentStr}/g" build.gradle

for state in ${list}
do
#    echo "this word is $state"
    commentStr="implementation \"com.knight"
    sed -i "" "s/${commentStr}/\/\/k ${commentStr}/g" ${state}/build.gradle
    appStr="apply plugin: 'com.knight"
    sed -i "" "s/${appStr}/\/\/k ${appStr}/g" ${state}/build.gradle

#    ./gradlew clean :${state}:uploadArchives --stacktrace 2>&1| tee log.txt
#    buildResult=`grep "BUILD FAILED" log.txt`
#    if [[ ${buildResult} =~ "BUILD FAILED" ]]
#    then
#        echo "please check your project"
#        rm -f log.txt
#        exit 1
#    fi
#    rm -f log.txt
done

