package com.modernfrontendshtmx.oobtimesheets.project;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {
    private final Map<Integer, Project> projects = new HashMap<>();

    public ProjectService() {
        projects.putAll(Stream.of(new Project(1, "CodeMorph"),
                                  new Project(2, "IntelliBot"),
                                  new Project(3, "SynthoGuard"))
                              .collect(Collectors.toMap(Project::id, Function.identity())));
    }

    public List<Project> getProjects() {
        return projects.values().stream()
                       .sorted(Comparator.comparing(Project::id))
                       .toList();
    }
}
