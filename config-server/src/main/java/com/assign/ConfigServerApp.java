package com.assign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config server to server external properties
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApp {

  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApp.class, args);
  }
}
