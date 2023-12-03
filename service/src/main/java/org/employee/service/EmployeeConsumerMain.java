package org.employee.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"org.employee.repository","org.employee.service"})
@EnableAutoConfiguration
public class EmployeeConsumerMain {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EmployeeConsumerMain.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run(args);
    }
}