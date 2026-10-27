package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.view.AiAssistRequest;
import com.example.HospitalManagementSystem.view.AiAssistResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiAssistantService {
    private static final String DISCLAIMER =
            "Dummy AI only. This is not a diagnosis or medical advice. A licensed clinician must review care decisions.";

    @Value("${app.ai.gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    @Value("${app.ai.ollama.model:llama3.2}")
    private String ollamaModel;

    public AiAssistResponse assist(AiAssistRequest request) {
        String provider = normalizeProvider(request.getProvider());
        String prompt = request.getPrompt() == null ? "" : request.getPrompt().trim();
        if (prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt is required");
        }

        String model = "GEMINI".equals(provider) ? geminiModel : ollamaModel;
        String answer = dummyAnswer(provider, model, prompt);
        return new AiAssistResponse(provider, model, "DUMMY", answer, DISCLAIMER);
    }

    private String normalizeProvider(String raw) {
        String provider = raw == null ? "GEMINI" : raw.trim().toUpperCase();
        if (!provider.equals("GEMINI") && !provider.equals("OLLAMA")) {
            throw new IllegalArgumentException("Provider must be GEMINI or OLLAMA");
        }
        return provider;
    }

    /**
     * Simulated Google Gemini and local Ollama replies.
     * Swap this method for RestClient calls when real keys/servers are available.
     */
    private String dummyAnswer(String provider, String model, String prompt) {
        String text = prompt.toLowerCase();
        String style = "GEMINI".equals(provider)
                ? "Google Gemini (dummy, " + model + ")"
                : "Ollama (dummy, " + model + " on localhost:11434)";

        if (containsAny(text, "fever", "cough", "cold", "headache", "pain", "symptom")) {
            return style + " suggests this as a general hint only:\n"
                    + "1. Note when symptoms started and how strong they are.\n"
                    + "2. Keep fluids up and rest.\n"
                    + "3. Book a MediConnect appointment if symptoms last more than 2–3 days, worsen, or include chest pain, trouble breathing, or a very high fever.\n"
                    + "Your note: \"" + prompt + "\"";
        }
        if (containsAny(text, "medicine", "tablet", "prescription", "paracetamol", "dose")) {
            return style + " medicine explainer (dummy):\n"
                    + "Take prescribed medicine exactly as the doctor wrote it. Do not change the dose yourself.\n"
                    + "Ask the pharmacist if you are pregnant, have kidney/liver disease, or take other drugs.\n"
                    + "Your question: \"" + prompt + "\"";
        }
        if (containsAny(text, "lab", "blood", "test", "report")) {
            return style + " lab helper (dummy):\n"
                    + "Lab numbers need the full report and a doctor. Abnormal values can have many causes.\n"
                    + "Bring the result to your MediConnect visit instead of self-treating.\n"
                    + "Your question: \"" + prompt + "\"";
        }
        return style + " assistant reply (dummy):\n"
                + "I can help draft a short hospital note, explain a term in simple words, or suggest what to tell the doctor.\n"
                + "Staff still confirm everything in the real record.\n"
                + "You asked: \"" + prompt + "\"";
    }

    private boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
