package com.modernfrontendshtmx.errorhandling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface NumbersApi {
    @GetExchange("/{number}") // <.>
    String getFact(@PathVariable("number") int number); // <.>
}
