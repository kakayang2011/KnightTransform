#!/bin/bash


moduleList=("knight-transform" "knight-byteK" "knight-doubleCheck-plugin" "knight-shrinkR-plugin" )
list=("TRANSFORM_VERSION" "BTYEK_VERSION" "DOUBLECHECK_VERSION" "SHINKR_VERSION")
for ((i=0;i<${#list[@]};i++))
do
    version=${list[$i]}
    moduleName=${moduleList[$i]}
    echo "this word is $version"
    # 查找TOOL_VERSION字符
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
    ./gradlew clean :${moduleName}:build :${moduleName}:bintrayUpload -PbintrayUser=${bintaryUser} -PbintrayKey=${bintaryKey} -PdryRun=false 2>&1 | tee log.txt

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
        rm -f log.txt
        exit 1
     fi
    rm -f log.txt
done

