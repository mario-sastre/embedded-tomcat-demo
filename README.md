# Overview

The goal of this project is domonstrate how frameworks like springboot can wrap everything inside of a fat jar instead of building a war that later needs to be deployed into a tomcat or any other sevlet container based server. 
It's well known that SpringBoot registers calls the Tomcat library to create a Tomcat sever and also does the configuration programatically, this allow us to build a whole web application as a simple jar. Yes, that's not the only thing that needs to be done, we also have to build a fat jar (also knowns as uber jar), however, the main principle of springboot is to get rid of the need to just build a war and later deploy it. I took the time to investigate about how this is perfomed; for anyone who has developed web application since the 2000s, this might be very clear for them, however this is not something clear for developers who directly started to work with SpringBoot. 

## Would you like to learn more about this in detail? 
If that's the case, I encourage you to read the article I wrote in substack, here is the link: https://open.substack.com/pub/mariosastre/p/what-i-wish-someone-had-explained?r=3aiadg&utm_campaign=post-expanded-share&utm_medium=post%20viewer

## Project structure
You will find 2 packages: 
- raw
- spring

### Raw
In the first one, you will find a single file that which focuses on creating a typical servelet and then, registering that one in a tomcat sever that is created programatically

### Spring
In this pacakge, you will find a different approach, since spring mvc has to register the dispatcher servlet in a web.xml and later we deploy the war to a tomcat server, the goal in this package was to get rid of that web.xml configuration and do it programatically by using the Tomcat library, same as the Raw package. 


## Compiling and Running
### Compiling
This is a maven project that is using the "quick start" archetype. In order to compile, just have to execute the following: 
```
mvn clean package
```

### Runnin the project
There are 2 implementations here that we can execute: 

#### Raw
```
java -cp target/embedded-tomcat-demo-1.0-SNAPSHOT.jar com.sastremario.practices.basic.raw.RawTomcatServer
```

This can be tested either with curl or with a simple browser: GET http://localhost:8080/hello

#### Spring
```
java -cp target/embedded-tomcat-demo-1.0-SNAPSHOT.jar com.sastremario.practices.basic.spring.DispatcherServletServer
```

This can be tested either with curl or with a simple browser: 
- GET http://localhost:8081/hello
- GET http://localhost:8081/health




