package com.modernfrontendshtmx.inlineediting.issue;

import java.util.List;

public class Issue {
    private String key;
    private String summary;
    private IssueType type;
    private IssuePriority priority;
    private String fixVersion;
    private List<SubTask> subTasks;

    public Issue(String key,
                 String summary,
                 IssueType type,
                 IssuePriority priority,
                 String fixVersion,
                 List<SubTask> subTasks) {
        this.key = key;
        this.summary = summary;
        this.type = type;
        this.priority = priority;
        this.fixVersion = fixVersion;
        this.subTasks = subTasks;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public IssueType getType() {
        return type;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
