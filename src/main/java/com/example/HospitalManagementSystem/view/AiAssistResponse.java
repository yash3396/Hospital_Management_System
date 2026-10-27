package com.example.HospitalManagementSystem.view;

public class AiAssistResponse {
    private String provider;
    private String model;
    private String mode;
    private String answer;
    private String disclaimer;

    public AiAssistResponse() {
    }

    public AiAssistResponse(String provider, String model, String mode, String answer, String disclaimer) {
        this.provider = provider;
        this.model = model;
        this.mode = mode;
        this.answer = answer;
        this.disclaimer = disclaimer;
    }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
}
