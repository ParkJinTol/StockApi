package com.example.demo.jstockapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JstockApiController {
  @GetMapping("/")
  public String Jstock() {
    return "hello";
  }

  @GetMapping("/test")
  public String Jstock1() {
    return "hello";
  }

}
