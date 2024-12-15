package dev.emircankirez.llama3;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/turkish-name")
    public String sayTurkishName(@RequestParam(value = "gender", defaultValue = "M") String gender) {
        String genderResult = gender.equals("M") ? "Male" : "Female";
        String userMessage = "Tell me a turkish " + genderResult + " name and explain its meaning";

        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
