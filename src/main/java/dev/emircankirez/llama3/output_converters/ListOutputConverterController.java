package dev.emircankirez.llama3.output_converters;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/list")
public class ListOutputConverterController {

    private final ChatClient chatClient;

    public ListOutputConverterController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping
    public List<String> list() {
        String message = """
                Give me the top 10 programming languages.
                {format}
                """;

        ListOutputConverter outputConverter = new ListOutputConverter(new DefaultConversionService());
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("format", outputConverter.getFormat()));

        return outputConverter.convert(chatClient.prompt(prompt).call().content());

    }

    @GetMapping("/list")
    public List<String> listProgrammingLanguages() {
        return chatClient.prompt()
                .user("Give me the top 10 programming languages.")
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

}
