package com.ex.restapipractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class RestapiPracticeApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ac =
        SpringApplication.run(RestapiPracticeApplication.class, args);

//    String[] all = ac.getBeanDefinitionNames();
//    for (String s : all) {
//      System.out.println(s);
//    }
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);
    return localeResolver;
  }

}
