package com.modernfrontendshtmx.inlineediting.issue.usecase;

import com.modernfrontendshtmx.inlineediting.issue.Issue;
import com.modernfrontendshtmx.inlineediting.issue.repository.IssueRepository;
import org.springframework.stereotype.Component;

@Component
public class GetIssueUseCase {
    private final IssueRepository repository;

    public GetIssueUseCase(IssueRepository repository) {
        this.repository = repository;
    }

    public Issue execute(String key) {
        return repository.getIssue(key);
    }
}
