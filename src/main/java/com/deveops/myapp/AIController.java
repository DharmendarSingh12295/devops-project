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

    String apiKey = System.getenv("ANTHROPIC_API_KEY");
    String prompt = request.get("prompt");

    String url = "https://api.anthropic.com/v1/messages";

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key", apiKey);
    headers.set("anthropic-version", "2023-06-01");
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = new HashMap<>();
    body.put("model", "claude-3-haiku-20240307");  // ✅ stable model
    body.put("max_tokens", 1000);

    // ✅ CORRECT FORMAT
    List<Map<String, Object>> messages = new ArrayList<>();

    Map<String, Object> message = new HashMap<>();
    message.put("role", "user");

    List<Map<String, String>> content = new ArrayList<>();
    Map<String, String> textObj = new HashMap<>();
    textObj.put("type", "text");
    textObj.put("text", prompt);

    content.add(textObj);
    message.put("content", content);

    messages.add(message);
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