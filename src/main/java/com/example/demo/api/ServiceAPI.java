package com.example.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ServiceAPI {

    @GetMapping
    public String serverRunning(){
        return "The Authentification server is up and running.";
    }

    
}
