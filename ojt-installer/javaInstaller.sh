#!/bin/sh

echo "Current folder : `pwd`"

ARTIFACT_ID=$1
VERSION=$2
JAVA_INSTALLER=jre-6u16-linux-i586.bin
JAVA_INSTALLED_FOLDER=jre1.6.0_16
BASE_FOLDER=target/$ARTIFACT_ID-$VERSION-bin-release-linux

echo "Copying java in install folder : $BASE_FOLDER"
cp target_install/$JAVA_INSTALLER $BASE_FOLDER/ojt
chmod +x $BASE_FOLDER/ojt/$JAVA_INSTALLER
cd $BASE_FOLDER/ojt
yes|./$JAVA_INSTALLER
mv $JAVA_INSTALLED_FOLDER java
rm $JAVA_INSTALLER