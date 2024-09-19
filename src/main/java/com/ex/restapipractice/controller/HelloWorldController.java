package com.ex.restapipractice.controller;

import com.ex.restapipractice.bean.HelloWorldBean;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {
  private MessageSource messageSource;

  public HelloWorldController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("/hello-world")
  public String helloworld() {
    return "hello";
  }

  @GetMapping("/hello-world-bean")
  public HelloWorldBean helloworldBean() {
    return new HelloWorldBean("hello-bean");
  }

  @GetMapping("/hello-world-bean/path-variable/{name}")
  public HelloWorldBean helloworldBeanPathVar(@PathVariable String name) {
    return new HelloWorldBean("hello-bean " + name);
  }

  @GetMapping("/hello-world-internationalized")
  public String helloworldInter(
      @RequestHeader(name = "Accept-Language", required = false) Locale locale) {

    return messageSource.getMessage("greeting.message", null, locale);
  }
}
