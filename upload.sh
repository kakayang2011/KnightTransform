#!/bin/bash

# 查找TOOL_VERSION字符
origin=`grep "TOOL_VERSION" gradle.properties | awk '{print $1}'`
notNeedChange=`grep "TOOL_VERSION" gradle.properties | cut -d '.' -f 1`
secondVersion=`grep "TOOL_VERSION" gradle.properties | cut -d '.' -f 2`
lastVersion=`grep "TOOL_VERSION" gradle.properties | cut -d '.' -f 3 `

str=${notNeedChange}"."${secondVersion}"."$[lastVersion+1]
echo ${str}

sed -i "" "s/${origin}/${str}/g" gradle.properties
#./gradlew clean build bintrayUpload -PbintrayUser=296777513 -PbintrayKey=0e35550c9145602f043f4cb98f2a12b9a6bbb98f -PdryRun=false
#sed -i "" "s/"SOURCE_DEPENDECY=true"/"SOURCE_DEPENDECY=false"/g" basetools/local.properties
echo "begin upload please wait!!!~~~"

bintaryUser=`grep "PbintrayUser" local.properties | cut -d '=' -f 2`
bintaryKey=`grep "PbintrayKey" local.properties | cut -d '=' -f 2`
./gradlew clean :tablayout:build :tablayout:bintrayUpload -PbintrayUser=${bintaryUser} -PbintrayKey=${bintaryKey} -PdryRun=false 2>&1 | tee log.txt

uploadStr=`grep "BUILD SUCCESSFUL" log.txt`

echo ${uploadStr}

if [[ ${uploadStr} =~ "BUILD SUCCESSFUL" ]]
then
    echo "========UPLOAD SUCCESS "
    git status
    git add ./gradle.properties
    git commit -m "feature: upload version code $str"
else
    echo "Failed~~~~ please wait~~"
    sed -i "" "s/${str}/${origin}/g" gradle.properties
#    ./gradlew clean :tablayout:build :tablayout:bintrayUpload -PbintrayUser=${bintaryUser} -PbintrayKey=${bintaryKey} -PdryRun=false
 fi
rm -f log.txt
# sed -i "" "s/"SOURCE_DEPENDECY=false"/"SOURCE_DEPENDECY=true"/g" basetools/local.properties

