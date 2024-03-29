package com.jstockapi.web;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/upbit")
@ConfigurationProperties(prefix = "ips")
public class JsUpBitApiController {
  @Value("${ips.ip1.accessKey}")
  private String accessKeyIp1;
  @Value("${ips.ip1.secretKey}")
  private String secretKeyIp1;
  @Value("${ips.ip2.accessKey}")
  private String accessKeyIp2;
  @Value("${ips.ip2.secretKey}")
  private String secretKeyIp2;

  @GetMapping("/account")
  public ModelAndView getJstock(Model model) {
    JsonNode rootData = null;
    String jwtToken = "";
    ModelAndView mv = new ModelAndView("upbit/upbit");
    try {
      InetAddress localHost = InetAddress.getLocalHost();
      String ipAddress = localHost.getHostAddress();
      if (ipAddress.equals("220.118.0.218")) {
        Algorithm algorithm = Algorithm.HMAC256(secretKeyIp1);
        jwtToken = JWT.create()
            .withClaim("access_key", accessKeyIp1)
            .withClaim("nonce", UUID.randomUUID().toString())
            .sign(algorithm);
      } else {
        Algorithm algorithm = Algorithm.HMAC256(secretKeyIp2);
        jwtToken = JWT.create()
            .withClaim("access_key", accessKeyIp2)
            .withClaim("nonce", UUID.randomUUID().toString())
            .sign(algorithm);
      }
      String serverUrl = "https://api.upbit.com";

      String authenticationToken = "Bearer " + jwtToken;

      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
      request.setHeader("Content-Type", "application/json");
      request.addHeader("Authorization", authenticationToken);

      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      String data = EntityUtils.toString(entity, "UTF-8");
      ObjectMapper objectMapper = new ObjectMapper();

      JsonNode rootNode = objectMapper.readTree(data);
      // 배열 안에 첫 번째 객체를 가져옴

      System.out.println("data 값: " + rootNode.size());
      for (int i = 0; i < rootNode.size(); i++) {
        rootData = rootNode.get(i);
      }

      // 필드 값을 가져옴
      JsonNode currencyNode = rootData.path("currency");
      JsonNode balanceNode = rootData.path("balance");
      JsonNode lockedNode = rootData.path("locked");
      JsonNode avg_buy_priceNode = rootData.path("avg_buy_price");
      JsonNode avg_buy_price_modifiedNode = rootData.path("avg_buy_price_modified");
      JsonNode unit_currencyNode = rootData.path("unit_currency");

      // "currency" 필드가 있는지 확인
      String currencyValue = currencyNode.asText();
      String balanceValue = balanceNode.asText();
      String lockedValue = lockedNode.asText();
      String avg_buy_priceValue = avg_buy_priceNode.asText();
      String avg_buy_price_modifiedValue = avg_buy_price_modifiedNode.asText();
      String unit_currencyValue = unit_currencyNode.asText();
      System.out.println("currencyValue 값: " + currencyValue);
      System.out.println("balanceValue 값: " + balanceValue);
      System.out.println("lockedValue 값: " + lockedValue);
      System.out.println("avg_buy_priceValue 값: " + avg_buy_priceValue);
      System.out.println("avg_buy_price_modifiedValue 값: " + avg_buy_price_modifiedValue);
      System.out.println("unit_currencyValue 값: " + unit_currencyValue);
      // 가져온 데이터를 모델에 추가
      mv.addObject("currencyValue", currencyValue);
      mv.addObject("balanceValue", balanceValue);
      mv.addObject("lockedValue", lockedValue);
      mv.addObject("avg_buy_priceValue", avg_buy_priceValue);
      mv.addObject("avg_buy_price_modifiedValue", avg_buy_price_modifiedValue);
      mv.addObject("unit_currencyValue", unit_currencyValue);
      return mv;
    } catch (IOException e) {
      return null;
    }
  }
}