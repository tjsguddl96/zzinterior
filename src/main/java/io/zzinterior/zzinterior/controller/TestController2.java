package io.zzinterior.zzinterior.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequiredArgsConstructor
public class TestController2 {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUrl;

    @PostMapping("/api/auth/google")
    public ResponseEntity<?> getAccessToken(@RequestBody Map<String, String> body) {
        System.out.println("==========================");
        for(String key:body.keySet()){
            System.out.println(key+" "+body.get(key));
        }
        System.out.println("==========================");


        String authorizationCode = body.get("code");

        // 구글에 액세스 토큰을 요청하기 위한 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        System.out.println("-----------------");
        for(String key: params.keySet()){
            System.out.println(key+" : "+params.get(key));
        }
        System.out.println("--------------");


        // 토큰을 요청하는 HTTP 엔티티 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //액세스 토큰을 반환
        return ResponseEntity.ok(response.getBody());
    }


}



