package com.management.building;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.management.building.entity.Space;

@SpringBootApplication
public class BuildingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildingApplication.class, args);
        Space space = Space.builder().area(1203.0).build();
        System.out.println(space);
    }

}
