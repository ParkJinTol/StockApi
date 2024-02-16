package com.example.demo.jstockapi.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class JstockApiController {
  @GetMapping("/")
  public String Jstock() {
    return "hello";
  }

}
