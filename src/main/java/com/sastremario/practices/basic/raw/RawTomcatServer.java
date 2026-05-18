package com.sastremario.practices.basic.raw;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.IOException;
import java.io.PrintWriter;

public class RawTomcatServer {

    public static void main(String[] args) throws LifecycleException {
        // create the tomcat instance (same library as standalone Tomcat, just
        // instantiated as a Java object inside our process)

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat-work"); // temp dir for Tomcat internals

        // Configure the http connector
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        // Create a web application context rooted at "/
        // equivalent to deploying a WAR at the root
        Context context = tomcat.addContext("", null);

        // Instantiate our servlet manually
        HelloServlet helloServlet = new HelloServlet();

        // Register it with tomcat, give a name and map it to a URL pattern
        // this is what web.xml <servlet> + <servlet-mapping> used to declare
        Tomcat.addServlet(context, "helloServlet", helloServlet);
        context.addServletMappingDecoded("/hello", "helloServlet");

        // let's start tomcat

        tomcat.start();

        System.out.println("─────────────────────────────────────────");
        System.out.println("  Raw Tomcat server started on port 8080");
        System.out.println("  GET http://localhost:8080/hello");
        System.out.println("─────────────────────────────────────────");

        // Block the main thread, same pattern springboot uses internally
        tomcat.getServer().await();

    }

    static class HelloServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            PrintWriter writer = resp.getWriter();
            writer.println("{");
            writer.println("  \"phase\": \"1 - Raw Tomcat\",");
            writer.println("  \"message\": \"No Spring. No magic. Just Tomcat + HttpServlet.\",");
            writer.println("  \"path\": \"" + req.getRequestURI() + "\"");
            writer.println("}");
        }
    }
}
