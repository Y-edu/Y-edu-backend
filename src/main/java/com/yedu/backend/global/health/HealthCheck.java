package com.yedu.backend.global.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
