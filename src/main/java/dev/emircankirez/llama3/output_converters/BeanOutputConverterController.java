package dev.emircankirez.llama3.output_converters;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bean")
public class BeanOutputConverterController {

    private final ChatClient chatClient;

    public BeanOutputConverterController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/songs/{singer}")
    public Singer getSongsBySinger(@PathVariable String singer) {
        String message = """
                Give me the songs of {singer}. If you don't know just say don't know. Here is the {format}.
                """;

        BeanOutputConverter<Singer> outputConverter = new BeanOutputConverter<>(Singer.class);
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("singer", singer, "format", outputConverter.getFormat()));
        return outputConverter.convert(chatClient.prompt(promptTemplate.create()).call().content());
    }
}
