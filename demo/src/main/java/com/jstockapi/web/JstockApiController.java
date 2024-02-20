package com.jstockapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JstockApiController {
  @GetMapping("/stock")
  public String Jstock() {
    return "stock";
  }
}
