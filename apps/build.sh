#!/bin/bash
mvn clean

mvn package -Dplatform.dependency=macosx-x86_64
mv target/sikuli-slides-1.5.0.jar target/sikuli-slides-1.5.0_macosx-x86_64.jar

mvn package -Dplatform.dependency=windows-x86_64
mv target/sikuli-slides-1.5.0.jar target/sikuli-slides-1.5.0_windows-x86_64.jar

mvn package -Dplatform.dependency=windows-x86
mv target/sikuli-slides-1.5.0.jar target/sikuli-slides-1.5.0_windows-x86.jar

mvn package -Dplatform.dependency=linux-x86_64
mv target/sikuli-slides-1.5.0.jar target/sikuli-slides-1.5.0_linux-x86_64.jar

mvn package -Dplatform.dependency=linux-x86
mv target/sikuli-slides-1.5.0.jar target/sikuli-slides-1.5.0_linux-x86.jar

mvn package -Dplatform.dependencies=true

