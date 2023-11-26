package com.modernfrontendshtmx.errorhandling;

import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Component
public class NumbersApiGateway {
    private static final RandomGenerator RANDOM_GENERATOR = RandomGeneratorFactory.getDefault().create();

    private final NumbersApi api;

    public NumbersApiGateway(NumbersApi api) {
        this.api = api;
    }

    public String getFact(int number) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int randomNumber = RANDOM_GENERATOR.nextInt(3); // <.>
        switch (randomNumber) {
            case 0 -> { // <.>
                return api.getFact(number);
            }
            case 1 -> { // <.>
                try {
                    Thread.sleep(5000);
                    return api.getFact(number);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            // <.>
            default -> throw new RuntimeException("Unable to get a fact about the number " + number);
        }
    }
}
