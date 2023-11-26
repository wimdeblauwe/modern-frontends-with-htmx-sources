package com.modernfrontendshtmx.ssedemo;

import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileProcessor {
    private final Set<ProgressListener> progressListeners = new HashSet<>();

    public void process(InputStream inputStream) throws IOException { //<.>
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            inputStream.transferTo(baos);
            try (InputStream firstClone = new ByteArrayInputStream(baos.toByteArray());
                 InputStream secondClone = new ByteArrayInputStream(baos.toByteArray())) {
                int numberOfLinesTotal = CharStreams.readLines(new InputStreamReader(firstClone)).size(); //<.>
                CharStreams.readLines(new InputStreamReader(secondClone),
                        new MyLineProcessor(numberOfLinesTotal)); //<.>
            }
        }
    }

    public void addProgressListener(ProgressListener progressListener) { //<.>
        progressListeners.add(progressListener);
    }

    public void removeProgressListener(ProgressListener progressListener) {
        progressListeners.remove(progressListener);
    }

    private static void sleepQuietly() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class MyLineProcessor implements LineProcessor<Void> { //<.>
        private final int numberOfLinesTotal;
        private int numberOfLinesProcessed = 0;

        private MyLineProcessor(int numberOfLinesTotal) {
            this.numberOfLinesTotal = numberOfLinesTotal;
        }

        @Override
        public boolean processLine(String line) throws IOException {
            // Fake some import work
            sleepQuietly();
            numberOfLinesProcessed++;
            notifyProgressListeners(line); //<.>
            return true;
        }

        @Override
        public Void getResult() {
            return null;
        }

        private void notifyProgressListeners(String line) {
            for (ProgressListener progressListener : progressListeners) {
                Progress progress = new Progress(numberOfLinesProcessed / (double) numberOfLinesTotal);
                progressListener.onProgress(progress, "Processing line : " + line);
            }
        }
    }
}
