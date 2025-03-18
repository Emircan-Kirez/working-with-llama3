package dev.emircankirez.llama3.function_calling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    @Bean
    @Description("Get the weather for the given city.")
    public Function<MockWeatherService.Request, MockWeatherService.Response> weatherService() {
        return new MockWeatherService();
    }
}
