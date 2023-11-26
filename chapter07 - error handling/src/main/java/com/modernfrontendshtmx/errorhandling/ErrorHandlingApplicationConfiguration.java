package com.modernfrontendshtmx.errorhandling;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ErrorHandlingApplicationConfiguration {
    @Bean
    public WebClient webClient() { // <.>
        return WebClient.builder()
                        .baseUrl("http://numbersapi.com/")
                        .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) { // <.>
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
    }

    @Bean
    public NumbersApi numbersApi(HttpServiceProxyFactory factory) { // <.>
        return factory.createClient(NumbersApi.class);
    }
}
