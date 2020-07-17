&larr; [Previous step: Implementing actions for creating Customers and Correspondence documents](./step7-custom-actions.md)

# Staging the release using Docker image

Customize the image name and tags (if necessary) at [`custom-docker/pom.xml`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-docker/pom.xml):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>u7-crm-app</artifactId>
        <groupId>com.intellective.sample</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>custom-docker</artifactId>
    <packaging>pom</packaging>

    <build>
        <plugins>
        <!-- ... -->
            <plugin>
                <groupId>io.fabric8</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>build-docker</id>
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <verbose>true</verbose>
                    <images>
                        <image>
                            <name>unity/sample-crm-app</name>       <!-- HERE -->
                            <build>
                                <tags>
                                    <tag>${project.version}</tag>   <!-- AND THERE -->
                                    <tag>latest</tag>
                                </tags>
                                <contextDir>${basedir}</contextDir>
                            </build>
                        </image>
                    </images>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <!-- ... -->
</project>

```

Build the production build of the application including Docker image: `mvn clean package -P docker`.

Test the containerized application using the image name and tag(s) defined above:
```
docker run --rm -p 9080:9080 unity/sample-crm-app:latest
```
Acceess http://localhost:9080/custom-webapp after it starts.

Use the image to stage the release through different environments using Docker Swarm, Kubernetes or OpenShift 
container orchestration. Override the configuration files using volumes if needed.

&rarr; *[OPTIONAL]* [Step X: Further exercises and improvements](./stepX-further-exercises.md)