package com.mmy.example.turbine.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: mmy
 * @date: 2018/04/28
 * @description:
 */
@SpringBootApplication
@EnableTurbine
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.mmy.example.turbine")
public class ApplicationStartup {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ApplicationStartup.class);
    springApplication.addListeners(new ApplicationPidFileWriter());
    springApplication.run(args);
  }
}
