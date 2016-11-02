#!/usr/bin/env bash
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
cd target && java -cp safs-1.0-SNAPSHOT-jar-with-dependencies.jsa filesystems.safs.slave.SlaveDriver $0