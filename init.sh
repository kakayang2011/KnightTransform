#!/usr/bin/env bash

echo "begin init project~~"

./gradlew clean :knight-transform:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#knight-byteK
sed -i "" "s/\/\/k//g" knight-byteK/build.gradle
./gradlew clean :knight-byteK:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt


#knight-doubleCheck-plugin
sed -i "" "s/\/\/k//g" knight-doubleCheck-plugin/build.gradle
./gradlew clean :knight-doubleCheck-plugin:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#knight-shrinkR-plugin
sed -i "" "s/\/\/k//g" knight-shrinkR-plugin/build.gradle
./gradlew clean :knight-shrinkR-plugin:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#knight-config-plugin
sed -i "" "s/\/\/k//g" knight-config-plugin/build.gradle
./gradlew clean :knight-config-plugin:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#knight-doubleCheck-library
sed -i "" "s/\/\/k//g" knight-doubleCheck-library/build.gradle
./gradlew clean :knight-doubleCheck-library:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#library
./gradlew clean :library:uploadArchives --stacktrace 2>&1| tee log.txt
buildResult=`grep "BUILD FAILED" log.txt`
if [[ ${buildResult} =~ "BUILD FAILED" ]]
then
    echo "please check your project"
    exit 1
fi
rm -f log.txt

#project build.gradle
sed -i "" "s/\/\/k//g" build.gradle

#app build.gradle
sed -i "" "s/\/\/k//g" app/build.gradle

./gradlew build