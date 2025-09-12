package com.management.building.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/spaces")
public class SpaceController {

    @GetMapping
    public String getHello() {
        return "Hello World";
    }

}
