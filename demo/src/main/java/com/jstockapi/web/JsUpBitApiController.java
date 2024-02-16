package com.jstockapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsUpBitApiController {
  @GetMapping("/upbit")
  public String Jstock() {
    return "upbit";
  }
}
