package com.modernfrontendshtmx.error_handling;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final JsonPlaceholderApiGateway jsonPlaceholderApiGateway;

    public HomeController(JsonPlaceholderApiGateway jsonPlaceholderApiGateway) { // <.>
        this.jsonPlaceholderApiGateway = jsonPlaceholderApiGateway;
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @HxRequest
    @GetMapping("/posts")
    public String getRandomNumberFact(Model model,
                                      Integer postId) { // <.>
        model.addAttribute("postTitle", jsonPlaceholderApiGateway.getPostTitle(postId)); // <.>
        return "fragments :: result"; // <.>
    }
}
