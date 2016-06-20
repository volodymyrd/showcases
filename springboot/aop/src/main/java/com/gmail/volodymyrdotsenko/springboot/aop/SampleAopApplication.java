package com.gmail.volodymyrdotsenko.springboot.aop;

import com.gmail.volodymyrdotsenko.springboot.aop.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Volodymyr Dotsenko on 6/20/16.
 */
@SpringBootApplication
public class SampleAopApplication implements CommandLineRunner {

    // Simple example shows how an application can spy on itself with AOP

    @Autowired
    private HelloWorldService helloWorldService;

    @Override
    public void run(String... args) {
        System.out.println(this.helloWorldService.getHelloMessage());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleAopApplication.class, args);
    }
}