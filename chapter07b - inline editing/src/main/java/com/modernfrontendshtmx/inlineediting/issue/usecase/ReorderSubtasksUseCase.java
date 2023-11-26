package com.modernfrontendshtmx.inlineediting.issue.usecase;

import com.modernfrontendshtmx.inlineediting.issue.Issue;
import com.modernfrontendshtmx.inlineediting.issue.SubTask;
import com.modernfrontendshtmx.inlineediting.issue.repository.IssueRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReorderSubtasksUseCase {
    private final IssueRepository repository;

    public ReorderSubtasksUseCase(IssueRepository repository) {
        this.repository = repository;
    }

    public Issue execute(String key,
                         int[] subTaskOrder) {
        Issue issue = repository.getIssue(key);
        List<SubTask> subTasks = issue.getSubTasks();
        List<SubTask> reordered = new ArrayList<>();
        for (int order : subTaskOrder) {
            reordered.add(subTasks.get(order));
        }
        issue.setSubTasks(reordered);
        repository.saveIssue(issue);
        return issue;
    }
}
