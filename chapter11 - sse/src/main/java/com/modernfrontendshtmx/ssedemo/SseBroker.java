package com.modernfrontendshtmx.ssedemo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.List;

@Component
public class SseBroker {
    private final Sinks.Many<ProgressEvent> eventPublisher;

    private final FileProcessor fileProcessor;
    private ProgressListener progressListener;

    public SseBroker(FileProcessor fileProcessor) { //<.>
        this.fileProcessor = fileProcessor;
        eventPublisher = createNewSink(); //<.>
    }

    @PostConstruct
    void init() {
        progressListener = (progress, message) ->
                eventPublisher.tryEmitNext(new ProgressEvent(progress, message)); //<.>
        fileProcessor.addProgressListener(progressListener); //<.>
    }

    @PreDestroy
    void destroy() {
        fileProcessor.removeProgressListener(progressListener); //<.>
    }

    // tag::subscribeToUpdates[]
    public Flux<List<ProgressEvent>> subscribeToUpdates() {
        return this.eventPublisher.asFlux()
                .buffer(Duration.ofMillis(500));
    }
    // end::subscribeToUpdates[]

    private static Sinks.Many<ProgressEvent> createNewSink() {
        return Sinks.many()
                .multicast()
                .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    public record ProgressEvent(Progress progress, String message) { //<.>
    }
}
