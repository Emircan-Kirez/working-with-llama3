package dev.emircankirez.llama3.prompts;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/prompt")
public class PromptController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/prompts.st")
    private Resource resource;

    public PromptController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/simple")
    public String getSimplePrompt(@RequestParam String sector) {
        String userMessage = """
                Tell me the most valuable companies in {sector} sector in the world. Just give me the top 3.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage);
        return chatClient.prompt(promptTemplate.create(Map.of("sector", sector))).call().content();
    }

    @GetMapping("/system")
    public String useSystemMessage() {
        UserMessage userMessage = new UserMessage("What is the square root of 4?");
        //UserMessage userMessage = new UserMessage("What is 999 times 999?");

        SystemMessage systemMessage = new SystemMessage(resource);

        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).call().content();
    }
}
