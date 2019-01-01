#!/usr/bin/env bash
echo "begin init project~~"

list="knight-byteK knight-doubleCheck-plugin knight-shrinkR-plugin knight-config-plugin app"

commentStr="classpath \"com.knight"
sed -i "" "s/${commentStr}/\/\/k ${commentStr}/g" build.gradle

for state in ${list}
do
    commentStr="implementation \"com.knight"
    sed -i "" "s/${commentStr}/\/\/k ${commentStr}/g" ${state}/build.gradle
    appStr="apply plugin: 'com.knight"
    sed -i "" "s/${appStr}/\/\/k ${appStr}/g" ${state}/build.gradle

done

