#!/bin/sh
# simple script to run all Weld versions from 2.0 up to latest.
# this should be replaced by build sever in the future

# fail with first failed test
set -e

# run clean package with all unit tests
function testCDI1 {
    echo "Running with OWB $1"
    mvn -PCDI1 -Dowb.version=$1 clean compile
    JAVA_HOME=/usr/lib/jvm/java-8-openjdk mvn -Ddcevm.test.arguments=-XXaltjvm=dcevm -Dowb.version=$1 -PCDI1 test
}

function testCDI2 {
    echo "Running with OWB $1"
    mvn -PCDI2 -Dowb.version=$1 clean package
}

### Run tests CDI1 on j8
testCDI1 1.7.0
testCDI1 1.7.5

### Run tests CDI2 on j11
testCDI2 2.0.7
testCDI2 2.0.8
testCDI2 2.0.9
testCDI2 2.0.10
testCDI2 2.0.11
testCDI2 2.0.12
testCDI2 2.0.13
