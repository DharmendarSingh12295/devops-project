package com.deveops.myapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AIController {

@PostMapping("/generate")
public ResponseEntity<?> generate(@RequestBody Map<String, String> request) {

    String apiKey = System.getenv("GROQ_API_KEY");
    String prompt = request.get("prompt");

    String url = "https://api.groq.com/openai/v1/chat/completions";

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = new HashMap<>();
    body.put("model", "llama3-8b-8192");  // fast & free

    List<Map<String, String>> messages = new ArrayList<>();
    Map<String, String> msg = new HashMap<>();
    msg.put("role", "user");
    msg.put("content", prompt);
    messages.add(msg);

    body.put("messages", messages);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    try {
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return ResponseEntity.ok(response.getBody());

    } catch (Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
} 
}