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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
        Flux<ServerSentEvent<String>> heartbeat = Flux.interval(Duration.ofSeconds(5)) //<.>
                .map(it -> ServerSentEvent.<String>builder().event("heartbeat").build()); //<.>
        Flux<List<ProgressEvent>> updates = broker.subscribeToUpdates();
        return Flux.merge(heartbeat, //<.>
                        updates
                                .flatMap(events -> Flux.just(
                                                createLogEvent(events),
                                                createProgressEvent(events))
                                        .filter(Objects::nonNull)
                                ))
                .doOnSubscribe(subscription -> LOGGER.debug("Subscription: {}", subscription))
                .doOnCancel(() -> LOGGER.debug("cancel"))
                .doOnError(throwable -> LOGGER.debug(throwable.getMessage(), throwable))
                .doFinally(signalType -> LOGGER.debug("finally: {}", signalType));
    }

    private ServerSentEvent<String> createLogEvent(List<ProgressEvent> events) {
        return ServerSentEvent.<String>builder()
                .event("log-event")
                .data(events.stream()
                        .map(progressEvent -> "<div>%s</div>".formatted(replaceNewLines(progressEvent.message())))
                        .collect(Collectors.joining()))
                .build();
    }

    private static ServerSentEvent<String> createProgressEvent(List<ProgressEvent> events) {
        return ServerSentEvent.<String>builder()
                .event("progress-event")
                .data(events.stream()
                        .max(Comparator.comparing(progressEvent -> progressEvent.progress().value()))
                        .map(progressEvent -> "<progress class=\"progress w-full h-6\" value=\"%d\" max=\"100\" ></progress>".
                                formatted((int) (progressEvent.progress().value() * 100)))
                        .orElse(null))
                .build();
    }
    // end::progress[]

    private String replaceNewLines(String message) {
        return message.replaceAll("\n", "<br>");
    }

}
