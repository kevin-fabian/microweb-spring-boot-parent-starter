package com.github.fabiankevin.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class AutoConfigTestApp {
    public static void main(String[] args) {
        SpringApplication.run(AutoConfigTestApp.class, args);
    }
}
