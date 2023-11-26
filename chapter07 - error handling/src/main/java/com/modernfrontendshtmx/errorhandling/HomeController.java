package com.modernfrontendshtmx.errorhandling;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final NumbersApiGateway numbersApiGateway;

    public HomeController(NumbersApiGateway numbersApiGateway) { // <.>
        this.numbersApiGateway = numbersApiGateway;
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @HxRequest
    @GetMapping("/number-facts")
    public String getRandomNumberFact(Model model,
                                      Integer number) { // <.>
        model.addAttribute("fact", numbersApiGateway.getFact(number)); // <.>
        return "fragments :: result"; // <.>
    }
}
