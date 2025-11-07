package com.project.citi_aid_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public int welcome(@RequestParam int a, @RequestParam int b) {
        return a+b;
    }
}
