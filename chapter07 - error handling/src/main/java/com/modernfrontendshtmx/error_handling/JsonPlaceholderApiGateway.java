package com.modernfrontendshtmx.error_handling;

import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Component
public class JsonPlaceholderApiGateway {
    private static final RandomGenerator RANDOM_GENERATOR = RandomGeneratorFactory.getDefault().create();

    private final JsonPlaceholderApi api;

    public JsonPlaceholderApiGateway(JsonPlaceholderApi api) {
        this.api = api;
    }

    public String getPostTitle(int postId) {
        int randomNumber = RANDOM_GENERATOR.nextInt(3); // <.>
        switch (randomNumber) {
            case 0 -> { // <.>
                return api.getPost(postId).title();
            }
            case 1 -> { // <.>
                try {
                    Thread.sleep(5000);
                    return api.getPost(postId).title();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            // <.>
            default -> throw new RuntimeException("Unable to get post title for id " + postId);
        }
    }
}
