package dev.emircankirez.llama3.stuffing_the_prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/v1/olympic")
public class OlympicController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olympicSportsPrompt;

    @Value("classpath:/docs/olympic-sports.txt")
    private Resource olympicSportsDoc;

    public OlympicController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem("You are the assistant for the 2024 Summer Olympics.")
                .build();
    }

    @GetMapping("/2024")
    public String get2024OlympicSports(
            @RequestParam(defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
            @RequestParam(defaultValue = "false") boolean stuffit
    ) throws IOException {
        String sports = olympicSportsDoc.getContentAsString(Charset.defaultCharset());

        return chatClient.prompt()
                .user(u -> u.text(olympicSportsPrompt)
                        .param("question", message)
                        .param("context", stuffit ? sports : "")
                ).call()
                .content();
    }
}
