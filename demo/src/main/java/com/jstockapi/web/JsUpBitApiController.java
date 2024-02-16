package com.jstockapi.web;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class JsUpBitApiController {

  @GetMapping("/upbit")
  public List<JsUpBitApiController> getJstock() {

    String accessKey = "47NUwvb2m8UwUrOT07xPCnFZMQnZPEqz7vbR8NDB";
    String secretKey = "NCnSgHj0jA6HzGMa4sbGY8RruZMbKduvRfIS8Lrl";
    String serverUrl = "https://api.upbit.com";

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    String jwtToken = JWT.create()
        .withClaim("access_key", accessKey)
        .withClaim("nonce", UUID.randomUUID().toString())
        .sign(algorithm);

    String authenticationToken = "Bearer " + jwtToken;

    try {
      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
      request.setHeader("Content-Type", "application/json");
      request.addHeader("Authorization", authenticationToken);

      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();

      return new Gson().fromJson(EntityUtils.toString(entity, "UTF-8"), new TypeToken<List<JsUpBitApiController>>() {
      }.getType());

    } catch (IOException e) {
      return null;
    }
  }
}