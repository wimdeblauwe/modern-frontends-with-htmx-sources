package com.modernfrontendshtmx.inline_editing.issue.usecase;

import com.modernfrontendshtmx.inline_editing.issue.Issue;
import com.modernfrontendshtmx.inline_editing.issue.repository.IssueRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateSummaryUseCase {
    private final IssueRepository repository;

    public UpdateSummaryUseCase(IssueRepository repository) {
        this.repository = repository;
    }

    public Issue execute(String key,
                         String summary) {
        Issue issue = repository.getIssue(key); // <.>
        issue.setSummary(summary); // <.>
        repository.saveIssue(issue); // <.>
        return issue;
    }
}
