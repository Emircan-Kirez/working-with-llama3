package dev.emircankirez.llama3.chat_memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/memory")
public class MemoryController {

    private final ChatClient chatClient;

    public MemoryController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(
                                new InMemoryChatMemory()
                        )
                )
                .build();
    }

    @GetMapping
    public String home(@RequestParam String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

}
