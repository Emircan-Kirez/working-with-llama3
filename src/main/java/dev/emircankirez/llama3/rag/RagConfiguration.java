package dev.emircankirez.llama3.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class RagConfiguration {

    private final static Logger LOGGER = Logger.getLogger(RagConfiguration.class.getName());

    @Value("classpath:/docs/olympic-faq.txt")
    private Resource faq;


    /**
     * to be able to use Ollama embedding model, just run the following command:
     * ollama pull mxbai-embed-large
     */

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        File vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            LOGGER.info("VectorStore file exists. Loading it.");
            simpleVectorStore.load(vectorStoreFile);
        } else {
            LOGGER.info("VectorStore file does not exist. Creating it.");
            TextReader textReader = new TextReader(faq);
            List<Document> documents = textReader.read();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);

            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + File.separator + "vectorstore.json";
        return new File(absolutePath);
    }
}
