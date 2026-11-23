package com.modernfrontendshtmx.error_handling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface JsonPlaceholderApi {
    @GetExchange("/posts/{id}") // <.>
    Post getPost(@PathVariable("id") int id); // <.>
}
