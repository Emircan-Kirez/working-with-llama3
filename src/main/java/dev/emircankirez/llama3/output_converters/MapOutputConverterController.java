package dev.emircankirez.llama3.output_converters;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/map")
public class MapOutputConverterController {

    private final ChatClient chatClient;

    public MapOutputConverterController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/{person}")
    public Map<String, Object> giveSocialMediaLinks(@PathVariable String person) {
        String message = """
                Give me the social media links of {person}. Use the {person} as key and the social media links as value.
                {format}
                """;

        MapOutputConverter outputConverter = new MapOutputConverter();
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("person", person, "format", outputConverter.getFormat()));
        return outputConverter.convert(chatClient.prompt(promptTemplate.create()).call().content());
    }
}
