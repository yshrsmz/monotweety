#!/bin/sh

cd /monotweety
./gradlew assembleDebug
./gradlew lintDebug 2>&1 | reviewdog -efm="%f:%l: %m" -ci="circle-ci"