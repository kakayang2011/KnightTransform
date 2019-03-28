#!/bin/bash

name=$1
if [[ $name == "transform" ]]
then
   moduleName="knight-transform"
   version="TRANSFORM_VERSION"
elif [[ $name == "tinyImage" ]]
then
   moduleName="knight-tinyImage-plugin"
   version="TINYIMAGE_VERSION"
elif [[ $name == "bytek" ]]
then
   moduleName="knight-byteK"
   version="BTYEK_VERSION"
elif [[ $name == "doublecheck" ]]
then
   moduleName="knight-doubleCheck-plugin"
   version="DOUBLECHECK_VERSION"
elif [[ $name == "shrinkr" ]]
then
   moduleName="knight-shrinkR-plugin"
   version="SHINKR_VERSION"
elif [[ $name == "component" ]]
then
   moduleName="knight-component-plugin"
   version="COMPONENT_VERSION"
elif [[ $name == "componentlibrary" ]]
then
   moduleName="knight-component-library"
   version="COMPONENT_LIBRARY_VERSION"
else
   echo "please input ./upload [transform | tinyImage | bytek | doublecheck | shrinkr | component | componentlibrary] "
   exit 1
fi

echo "this word is $version"
origin=`grep ${version} gradle.properties | awk '{print $1}'`
notNeedChange=`grep ${version} gradle.properties | cut -d '.' -f 1`
secondVersion=`grep ${version} gradle.properties | cut -d '.' -f 2`
lastVersion=`grep ${version} gradle.properties | cut -d '.' -f 3 `

str=${notNeedChange}"."${secondVersion}"."$[lastVersion+1]
echo ${str}

sed -i "" "s/${origin}/${str}/g" gradle.properties
echo "begin upload please wait!!!~~~"

sed -i "" "s/\/\/k//g" ${moduleName}/build.gradle
bintaryUser=`grep "PbintrayUser" local.properties | cut -d '=' -f 2`
bintaryKey=`grep "PbintrayKey" local.properties | cut -d '=' -f 2`
./gradlew clean :${moduleName}:build :${moduleName}:bintrayUpload -PbintrayUser=${bintaryUser} -PbintrayKey=${bintaryKey} -PdryRun=false --stacktrace 2>&1 | tee log.txt

uploadStr=`grep "BUILD SUCCESSFUL" log.txt`

echo ${uploadStr}

if [[ ${uploadStr} =~ "BUILD SUCCESSFUL" ]]
then
    echo "========UPLOAD SUCCESS "
    git status
    git add ./gradle.properties
    git commit -m "feature: upload version code $str"
else
    echo "Failed~~~~ please check~~"
    sed -i "" "s/${str}/${origin}/g" gradle.properties
    rm -f log.txt
    exit 1
 fi
rm -f log.txt

#moduleList=("knight-transform" "knight-byteK" "knight-doubleCheck-plugin" "knight-shrinkR-plugin" )
#list=("TRANSFORM_VERSION" "BTYEK_VERSION" "DOUBLECHECK_VERSION" "SHINKR_VERSION")
#for ((i=0;i<${#list[@]};i++))
#do
#    version=${list[$i]}
#    moduleName=${moduleList[$i]}
#
#done

