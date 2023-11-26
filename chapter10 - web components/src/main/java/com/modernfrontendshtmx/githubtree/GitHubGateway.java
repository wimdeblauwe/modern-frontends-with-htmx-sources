package com.modernfrontendshtmx.githubtree;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GitHubGateway {
    public List<String> getRepositories(String username) {
        try {
            GitHub github = GitHub.connectAnonymously(); //<.>
            GHUser githubUser = github.getUser(username); //<.>
            PagedIterable<GHRepository> ghRepositories = githubUser.listRepositories(); //<.>
            return ghRepositories.toList().stream()
                    .map(GHRepository::getName)
                    .toList(); //<.>
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RepositoryRelease> getRepositoryReleases(String username, String repositoryName) {
        try {
            GitHub github = GitHub.connectAnonymously();
            GHUser wimdeblauwe = github.getUser(username);
            GHRepository repository = wimdeblauwe.getRepository(repositoryName); //<.>
            return repository.listReleases().toList()
                    .stream()
                    .map(ghRelease -> new RepositoryRelease(
                            ghRelease.getId(),
                            ghRelease.getName()))
                    .toList(); //<.>
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // tag::getRepositoryRelease[]
    public String getRepositoryRelease(String username,
                                       String repositoryName,
                                       long releaseId) {
        try {
            GitHub github = GitHub.connectAnonymously();
            GHUser wimdeblauwe = github.getUser(username);
            GHRepository repository = wimdeblauwe.getRepository(repositoryName);
            return repository.getRelease(releaseId).getBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // end::getRepositoryRelease[]

    public record RepositoryRelease(long id, String name) {
    }

}
