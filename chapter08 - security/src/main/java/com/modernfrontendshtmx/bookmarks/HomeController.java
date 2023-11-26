package com.modernfrontendshtmx.bookmarks;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {
    private final AtomicInteger autoIncrementCounter = new AtomicInteger(); //<.>
    private final Map<Integer, Bookmark> bookmarkMap = new HashMap<>(); //<.>

    @GetMapping
    public String index(Model model) {
        model.addAttribute("bookmarks", bookmarkMap.values()); //<.>
        return "index";
    }

    @PostMapping("/bookmarks/create") //<.>
    public String createBookmark(@ModelAttribute("formData") CreateBookmarkFormData formData) { //<.>
        addBookmark(formData);

        return "redirect:/"; //<.>
    }

    // tag::htmxCreateBookmark[]
    @HxRequest
    @PostMapping("/bookmarks")
    public String htmxCreateBookmark(@ModelAttribute("formData") CreateBookmarkFormData formData,
        Model model) {
        Bookmark bookmark = addBookmark(formData); //<.>

        model.addAttribute("bookmark", bookmark); //<.>
        return "index :: bookmark"; //<.>
    }
    // end::htmxCreateBookmark[]

    // tag::deleteBookmark[]
    @HxRequest
    @DeleteMapping("/bookmarks/{id}")
    @ResponseBody //<.>
    public String deleteBookmark(@PathVariable int id) {
        bookmarkMap.remove(id); //<.>

        return ""; //<.>
    }
    // end::deleteBookmark[]

    private Bookmark addBookmark(CreateBookmarkFormData formData) {
        Bookmark bookmark = formData.toBookmark(autoIncrementCounter.incrementAndGet());
        bookmarkMap.put(bookmark.id(), bookmark);
        return bookmark;
    }
}
