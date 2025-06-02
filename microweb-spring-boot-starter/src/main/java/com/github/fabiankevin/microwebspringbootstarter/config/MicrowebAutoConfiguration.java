package com.github.fabiankevin.microwebspringbootstarter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
public class MicrowebAutoConfiguration {
}
