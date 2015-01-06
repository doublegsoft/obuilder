<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
  http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
 
    <groupId>$groupId$</groupId>
    <artifactId>$artifactId$-bundle</artifactId>
    <version>$version$</version>
    <packaging>bundle</packaging> <!-- we want to produce an OSGi bundle -->
 
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
 
    <dependencies>
        <!-- specify the dependency you want to wrap as OSGi bundle -->
        <dependency>
            <groupId>$groupId$</groupId>
            <artifactId>$artifactId$</artifactId>
            <version>$version$</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
 
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.felix</groupId>
                <artifactId>maven-bundle-plugin</artifactId>
                <version>2.3.7</version>
                <extensions>true</extensions>
                <configuration>
                    <instructions>
                        <!-- export the packages that should be externally accessible -->
                        <Export-Package>$exportPackages$</Export-Package>
                    </instructions>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>