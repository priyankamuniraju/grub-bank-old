package com.grubbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.grubbank")
public class GrubbankApplication {

  public static void main(String[] args) {
    SpringApplication.run(GrubbankApplication.class, args);
  }
}
