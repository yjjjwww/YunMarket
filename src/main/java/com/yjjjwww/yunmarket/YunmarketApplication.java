package com.yjjjwww.yunmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YunmarketApplication {

  public static void main(String[] args) {
    SpringApplication.run(YunmarketApplication.class, args);
  }

}
