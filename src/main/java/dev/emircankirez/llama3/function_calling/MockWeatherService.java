package dev.emircankirez.llama3.function_calling;

import java.util.function.Function;

public class MockWeatherService implements Function<MockWeatherService.Request, MockWeatherService.Response> {

    public record Request(String city) {}
    public record Response(String weather, String temperature) {}

    @Override
    public Response apply(Request request) {
        // We can do whatever we want here, but for the sake of simplicity, we will return a hardcoded response.
        return new Response("Sunny", "25");
    }
}
