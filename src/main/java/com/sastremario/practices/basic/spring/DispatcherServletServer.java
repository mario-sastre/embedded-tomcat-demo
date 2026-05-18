package com.sastremario.practices.basic.spring;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Mario Sastre
 *
 * Manual Dispatcher Servlet registratio
 *
 * We manually do exactly what springboot's autoconfiguration does:
 * 1. Create an Application Context
 * 2. Wrap it in a DispatcherServlet
 * 3. Registert that servlet into an embedded Tomcat instance
 * 4. Start the server
 *
 *
 * Spring boot automates all those steps via:
 * - DispatcherServletAutoConfiguration -> creates the DispatcherServlet bean
 * - TomcatServletWebServerFactory -> creates and configures embedded tomcat
 * - SerlvetWebServerApplicationContext -> wires them together in onRefresh() method
 *
 * To test it, we have to run this:
 *
 * mvn clean package
 * java -cp target/embedded-tomcat-demo-1.0-SNAPSHOT.jar com.sastremario.practices.basic.spiring.DispatcherServletServer
 *
 * and then we can test the endpoints with curl:
 *
 * curl http://localhost:8081/hello
 * curl http://localhost:8081/health
 */

public class DispatcherServletServer {

    public static void main(String[] args) throws LifecycleException {
        // Step 1: Build the Spring application context
        // AnnotationConfigWebApplicationContext is the web aware version of the context. It can
        // scan @Controller, @Service, @Configuration classes. Spring boot uses ServletWebServerApplicationContext
        // which extends this and adds the embedded server lifecycle on top.

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        // Register our @Controller so spring knows about it.
        // Spring Boot does this via component scanning triggered by @SpringBootApplication
        context.register(HelloController.class);

        // Also register Spring MVC's default configuration (HandlerMapping, HandlerAdapter
        // Jackson message converter, etc)
        // Spring Boot does this via WebMvcAutoConfiguration
        context.register(org.springframework.web.servlet.config.annotation
                .WebMvcConfigurationSupport.class);

        // step 2: Create the DispatcherServlet
        // DispatcherServlet is the front controller. Every HTTP request foes through it:
        // it finds the right @Controller method, invokes it, serializes the response and wires it back.
        // Spring boot creates this in DispatcherServletAutoConfiguration as a DispatcherServletRegistrationBean
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        // step 3: Create and configure embedded tomcat
        // Identical to phase 1, same Tomcat API, same embedded library.
        // Spring boot wraps this in TomcatServletWebServerFactory, which also
        // handles SSL, compression, access logs, and custom connectors
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat-work-spring");

        Connector connector = new Connector();
        connector.setPort(8081);
        tomcat.setConnector(connector);

        Context tomcatContext = tomcat.addContext("", null);

        // step 4: Register DispatcherServlet into tomcat
        // This is the critical wiring step.
        // In a traditional WAR + web.xml setup, you would declare this in XML
        // Spring boot does this programatically via ServletRegistrationBean
        // inside TomcatServletWebServerFactory.getWebSever()

        // the "/*" mapping means DispatcherServlet handles ALL requests, exactly the default Spring Boot behavior
        Tomcat.addServlet(tomcatContext, "dispatcherServlet", dispatcherServlet);
        tomcatContext.addServletMappingDecoded("/*", "dispatcherServlet");

        // step 5: Start tomcat

        // In Spring Boot, this happens inside
        // ServletWebServerApplicationContext.onRefresh(), called during
        // ApplicationContext.refresh() — after all beans are instantiated
        // but before the app is considered fully started.
        tomcat.start();

        System.out.println("──────────────────────────────────────────────────────");
        System.out.println("  Spring MVC server started on port 8081");
        System.out.println("  GET http://localhost:8081/hello");
        System.out.println("  GET http://localhost:8081/health");
        System.out.println();
        System.out.println("  What just happened (the Spring Boot equivalent):");
        System.out.println("  AnnotationConfigWebApplicationContext  → ApplicationContext");
        System.out.println("  new DispatcherServlet(context)         → DispatcherServletAutoConfiguration");
        System.out.println("  Tomcat.addServlet(...)                 → TomcatServletWebServerFactory");
        System.out.println("  tomcat.start()                         → onRefresh() hook");
        System.out.println("──────────────────────────────────────────────────────");

        tomcat.getServer().await();
    }
}
