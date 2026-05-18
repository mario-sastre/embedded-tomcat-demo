package com.sastremario.practices.basic.spring;

/*
    * @author Mario Sastre
    * A plain Spring MVC Controller, Notice there is no main() here,
    * no auto configuration. This controller is wired manually by our
    * DispatcherServletServer, just like springboot does, but we
    * are doing it by hand so that we can see each step of the process
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public Map<String, String> hello(){
        return Map.of(
                "phase", "2 - Spring MVC DispatcherServlet",
                "message", "DispatcherServlet registered manually. No springboot involved",
                "handler", this.getClass().getSimpleName() + ".hello()"
        );
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health(){
        return Map.of(
                "status", "ok",
                "handler", this.getClass().getSimpleName() + ".health()"
        );
    }
}
