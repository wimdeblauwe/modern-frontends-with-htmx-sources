package com.modernfrontendshtmx.contactsapp.contact.service;

import org.springframework.util.Assert;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public final class ArchiveProcessInfo {
    private final AtomicInteger progress = new AtomicInteger(0);
    private Status status = Status.RUNNING;
    private Future<String> future;

    public int getProgress() {
        return progress.intValue();
    }

    public void setProgress(int progress) {
        Assert.isTrue(progress >= 0 && progress <= 100, "Progress should be between 0 and 100");
        this.progress.set(progress);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Future<String> getFuture() {
        return future;
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }

    enum Status { // <.>
        RUNNING,
        COMPLETE,
        FAILED
    }
}
