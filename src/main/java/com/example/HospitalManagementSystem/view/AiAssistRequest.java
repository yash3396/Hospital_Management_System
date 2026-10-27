package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotBlank;

public class AiAssistRequest {
    @NotBlank
    private String prompt;

    /** GEMINI or OLLAMA */
    private String provider = "GEMINI";

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
