package com.modernfrontendshtmx.githubtree;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final String USERNAME = "wimdeblauwe";

    private final GitHubGateway gateway;

    public HomeController(GitHubGateway gateway) {
        this.gateway = gateway;
    }

    // tag::repoTree[]
    @GetMapping
    public String index(Model model) { //<.>
        return "index";
    }

    @HxRequest
    @GetMapping("/repositories") //<.>
    public String repositoriesTree(Model model) {
        List<String> repositories = gateway.getRepositories(USERNAME); //<.>
        model.addAttribute("repositories", repositories);
        return "repositories-tree :: repositories"; //<.>
    }
    // end::repoTree[]

    // tag::repositoryReleasesTree[]
    @HxRequest
    @GetMapping("/repositories/{id}/releases")
    public String repositoryReleasesTree(@PathVariable("id") String id,
                                         Model model) {
        List<GitHubGateway.RepositoryRelease> releases = gateway.getRepositoryReleases(USERNAME, id);
        model.addAttribute("repositoryName", id);
        model.addAttribute("releases", releases);
        return "repositories-tree :: releases";
    }
    // end::repositoryReleasesTree[]

    // tag::repositoryReleaseNotes[]
    @HxRequest
    @GetMapping("/repositories/{name}/releases/{id}")
    public String repositoryReleaseNotes(@PathVariable("name") String repositoryName,
                                         @PathVariable("id") long releaseId,
                                         Model model) {
        String repositoryRelease = gateway.getRepositoryRelease(USERNAME, repositoryName, releaseId); //<.>
        model.addAttribute("releaseBody", renderMarkdown(repositoryRelease)); //<.>
        return "repositories-tree :: release-body"; //<.>
    }

    private String renderMarkdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    // end::repositoryReleaseNotes[]
}
