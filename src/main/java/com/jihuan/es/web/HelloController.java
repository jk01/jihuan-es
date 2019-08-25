package com.jihuan.es.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @RequestMapping("/es")
    public String index() {
        return "Hello World!";
    }
    
}
