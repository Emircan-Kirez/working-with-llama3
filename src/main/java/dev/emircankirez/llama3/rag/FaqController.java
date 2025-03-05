package dev.emircankirez.llama3.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/faq")
public class FaqController {

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    private final VectorStore vectorStore;
    private final ChatClient oldWayChatClient;
    private final ChatClient modernWayChatClient;

    public FaqController(ChatClient.Builder oldWayChatClient, ChatClient.Builder modernWayChatClient, VectorStore vectorStore) {
        this.oldWayChatClient = oldWayChatClient.build();
        this.modernWayChatClient = modernWayChatClient
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/old-way")
    public String oldWay(@RequestParam(defaultValue = "Is it true that more than 10,000 atheletes compete in the Olympic Games Paris 2024?") String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contents = similarDocuments.stream().map(Document::getContent).toList();
        String documentsString = String.join("\n", contents);
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate, Map.of("input", message, "documents", documentsString));
        return oldWayChatClient.prompt(promptTemplate.create()).call().content();
    }

    @GetMapping("/modern-way")
    public String modernWay(@RequestParam(defaultValue = "Is it true that more than 10,000 atheletes compete in the Olympic Games Paris 2024?") String message) {
        // You can use the ragPromptTemplate to create a prompt for the modern way as well
        return modernWayChatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
