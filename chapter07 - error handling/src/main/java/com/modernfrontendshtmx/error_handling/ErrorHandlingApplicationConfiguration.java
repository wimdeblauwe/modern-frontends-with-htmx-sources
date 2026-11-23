package com.modernfrontendshtmx.error_handling;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "jsonplaceholder", types = JsonPlaceholderApi.class)
public class ErrorHandlingApplicationConfiguration {
}
