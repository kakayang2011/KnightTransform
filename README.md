# ShinkR
[ ![Download](https://api.bintray.com/packages/knight/maven/shrinkR/images/download.svg?version=1.0.1) ](https://bintray.com/knight/maven/shrinkR/1.0.1/link)
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

An Android gradle plugin to delete redunent Filed in R*.class with bytecode

## Function

The purpose of this plugin is reduce the volume of apk. when we reference a layout or resource, usually we use these resource like this :

```kotlin
setContentView(R.layout.activity_layout)

val button = findViewById(R.id.button)

button.setBackgroundResource(R.drawable.bg)
```
From these code , wo can know that compiler reproduce the file named R.java, this file store all reference of resource. 
But after **compile** , these code will be compiled to **class** file, code like this:
```kotlin
setContentView(12345678)

val button = findViewById(12345679)

button.setBackgroundResource(12345671)
```
When code is compiled to apk, the R.java is redundant, and it is very large , so we need to delete these field in R.java.

## Setup

Add the following code to your **project's** build.gradle

```gradle
dependencies {
        ...
        classpath "com.knight.shrinkR:shrinkR:$SHINKR_VERSION"
        ...
    }
```

Add the following code to your **module's** build.gradle

```gradle
...
apply plugin: 'com.knight.transform.shrinkr'
...

```

Finally, wo just run this app in your phone, and it works.

## Comparison

Plugin|Apk's volume 
---|---
ShrinkR | 2.1M 
Normal | 2.2M

My project is a empty's App, if is a big app , this plugin can reduce the volume even more.

# DoubleCheck
[ ![Download](https://api.bintray.com/packages/knight/maven/doublecheck/images/download.svg?version=1.0.1) ](https://bintray.com/knight/maven/doublecheck/1.0.1/link)
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

This plugin of gradle is checking click's event, prevent quick clicks multiple times.

## Function

Consider a situation: when we click a button  quickly, the button's function is to open other activity, we will open this activity twice. Obviously this is not what we want, so we need check the click's event, we use bytecode instrumentation.

If we don't want check click quickly, we can add a annotation in this method, just like this:

```kotlin
button.setOnClickListener(new View.OnClickListener() {
            @DoubleCheck // we can add this annotation
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click: " + (++i), Toast.LENGTH_LONG).show();
            }
        });
```

## Setup

Add the following code to your **project's** build.gradle

```gradle
dependencies {
        ...
        classpath "com.knight.doublecheck:doublecheck:1.0.0"
        ...
    }
```

Add the following code to your **module's** build.gradle

```gradle
...
apply plugin: 'com.knight.transform'
...

dependencies {
    ...
    "com.knight.doublecheck:doublechecklibrary:1.0.1"
    ...
}

```

# How to run this Demo

I have already finish a script that can build aar, and upload to local ,you need run this script in your terminal, just like this :

```
./init.sh
```
If there no errors in the compliation process, you can run "app" to your Phone, and check the different.

# TO BE CONTUNUE


# Improve

If you have some problem or advice, please don't hesitate to raise an issue. Just have fun and hope this can help you.

# License 

> Apache Version 2.0
>
> Copyright 2018 Knight
>
> Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
> 
> http://www.apache.org/licenses/LICENSE-2.0
> 
> Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License