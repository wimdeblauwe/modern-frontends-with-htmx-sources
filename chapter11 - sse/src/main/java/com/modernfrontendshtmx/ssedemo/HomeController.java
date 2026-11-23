package com.modernfrontendshtmx.ssedemo;

import com.modernfrontendshtmx.ssedemo.SseBroker.ProgressEvent;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private final FileProcessor fileProcessor;
    private final SseBroker broker;

    public HomeController(FileProcessor fileProcessor, SseBroker broker) { //<.>
        this.fileProcessor = fileProcessor;
        this.broker = broker;
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @HxRequest
    @PostMapping
    @ResponseBody //<.>
    public void runImport(@RequestParam("file") MultipartFile file) throws IOException { //<.>
        fileProcessor.process(file.getInputStream()); //<.>
    }

    // tag::progress[]
    @GetMapping("/progress")
    public Flux<ServerSentEvent<String>> progress() {
        Flux<ServerSentEvent<String>> heartbeat = Flux.interval(Duration.ZERO, Duration.ofSeconds(5)) //<.>
                .map(it -> ServerSentEvent.<String>builder().comment("heartbeat").build()); //<.>
        Flux<List<ProgressEvent>> updates = broker.subscribeToUpdates();
        return Flux.merge(heartbeat, //<.>
                        updates
                                .filter(events -> !events.isEmpty())
                                .map(events -> ServerSentEvent.<String>builder()
                                        .data(createLogHtml(events) + createProgressBarHtml(events))
                                        .build()
                                ))
                .doOnSubscribe(subscription -> LOGGER.debug("Subscription: {}", subscription))
                .doOnCancel(() -> LOGGER.debug("cancel"))
                .doOnError(throwable -> LOGGER.debug(throwable.getMessage(), throwable))
                .doFinally(signalType -> LOGGER.debug("finally: {}", signalType));
    }

    private String createLogHtml(List<ProgressEvent> events) {
        return events.stream()
                .map(progressEvent -> "<div>%s</div>".formatted(replaceNewLines(progressEvent.message())))
                .collect(Collectors.joining());
    }

    private String createProgressBarHtml(List<ProgressEvent> events) {
        ProgressEvent lastEvent = events.getLast();
        int percentage = (int) (lastEvent.progress().value() * 100);
        return """
                <div id="progress-bar" hx-swap-oob="innerHTML">
                    <progress class="progress progress-primary w-full h-6" value="%d" max="100"></progress>
                </div>""".formatted(percentage);
    }
    // end::progress[]

    private String replaceNewLines(String message) {
        return message.replaceAll("\n", "<br>");
    }

}
