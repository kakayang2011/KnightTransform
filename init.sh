#!/usr/bin/env bash

#echo "begin init project~~"


plugin=("knight-tinyImage-plugin" )
pluginSwitch=("tinyImage" )

for (( i = 0; i < ${#plugin[@]}; ++i )); do
    state=${plugin[i]}
    switch=${pluginSwitch[i]}
    ./gradlew clean :${state}:uploadArchives --stacktrace 2>&1| tee log.txt
    buildResult=`grep "BUILD FAILED" log.txt`
    if [[ ${buildResult} =~ "BUILD FAILED" ]]
    then
        echo "please check your project"
        rm -f log.txt
        exit 1
    else
        localProperties=`grep ${switch} local.properties`
        if [[ ${buildResult} =~ "BUILD FAILED" ]]
        then
            echo "already exit ${switch} word"
        else
            line=`sed -n "$=" local.properties`
            sed -i "" "$line a\\
            $switch=true" local.properties
        fi
    fi
    rm -f log.txt
done

