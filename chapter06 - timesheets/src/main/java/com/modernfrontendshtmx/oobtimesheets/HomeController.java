package com.modernfrontendshtmx.oobtimesheets;

import com.modernfrontendshtmx.oobtimesheets.project.Project;
import com.modernfrontendshtmx.oobtimesheets.project.ProjectService;
import com.modernfrontendshtmx.oobtimesheets.timeregistration.TimeRegistrationService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/")
public class HomeController {
    private final ProjectService projectService;
    private final TimeRegistrationService timeRegistrationService;

    public HomeController(ProjectService projectService,
                          TimeRegistrationService timeRegistrationService) {
        this.projectService = projectService;
        this.timeRegistrationService = timeRegistrationService;
    }

    // tag::index[]
    @GetMapping
    public String index(Model model,
                        Locale locale) {
        model.addAttribute("projects", projectService.getProjects());
        List<LocalDate> daysOfCurrentWeek = getDaysOfCurrentWeek(locale);
        model.addAttribute("days", daysOfCurrentWeek);
        model.addAttribute("total", getTotal(daysOfCurrentWeek));
        for (LocalDate localDate : daysOfCurrentWeek) {
            model.addAttribute("dayTotal_" + localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), // <.>
                               getTotal(List.of(localDate))); // <.>
        }
        return "index";
    }
    // end::index[]

    // tag::update[]
    @HxRequest
    @PutMapping("/projects/{projectId}/{date}")
    public HtmxResponse updateTimeRegistration(@PathVariable int projectId,
                                               @PathVariable LocalDate date,
                                               Double value,
                                               Model model,
                                               Locale locale) {
        Duration duration = value == null ? Duration.ZERO : Duration.ofMinutes((long) (value * 60.0));
        timeRegistrationService.addOrUpdateRegistration(projectId, date, duration);

        model.addAttribute("total", getTotal(getDaysOfCurrentWeek(locale)));

        model.addAttribute("day", date); // <.>
        model.addAttribute("dayTotal_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                           getTotal(List.of(date)));  //<.>

        return HtmxResponse.builder()
                .view("index :: #overall-total")
                .view("index :: day-total")
                .build();  //<.>
    }
    // end::update[]


    // tag::total[]

    private Duration getTotal(List<LocalDate> daysOfCurrentWeek) {
        Set<Integer> projectIds = projectService.getProjects().stream().map(Project::id).collect(Collectors.toSet());
        return timeRegistrationService.getTotal(projectIds, Set.copyOf(daysOfCurrentWeek));
    }
    // end::total[]

    private static List<LocalDate> getDaysOfCurrentWeek(Locale locale) {
        LocalDate now = LocalDate.now();
        DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
        LocalDate firstDay = now.with(firstDayOfWeek);
        return Stream.iterate(firstDay, date -> date.plusDays(1))
                     .limit(7)
                     .toList();
    }
}
