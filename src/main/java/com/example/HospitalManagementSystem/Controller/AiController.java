package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.service.AiAssistantService;
import com.example.HospitalManagementSystem.view.AiAssistRequest;
import com.example.HospitalManagementSystem.view.AiAssistResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiAssistantService aiAssistantService;

    public AiController(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/assist")
    public ResponseEntity<AiAssistResponse> assist(@Valid @RequestBody AiAssistRequest request) {
        return ResponseEntity.ok(aiAssistantService.assist(request));
    }
}
