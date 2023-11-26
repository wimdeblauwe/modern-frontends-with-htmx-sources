package com.modernfrontendshtmx.ssedemo;

import org.springframework.util.Assert;

public record Progress(double value) {
    public Progress {
        Assert.isTrue(value >= 0.0 && value <= 1.0, "Progress value should be between 0.0 and 1.0");
    }
}
