package com.modernfrontendshtmx.oobtimesheets.timeregistration;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TimeRegistrationService {
    private final Map<ProjectDate, Duration> registrations = new HashMap<>();

    public void addOrUpdateRegistration(int projectId,
                                        LocalDate date,
                                        Duration duration) {
        registrations.put(new ProjectDate(projectId, date), duration);
    }

    public Duration getTotal(Set<Integer> projectIds,
                             Set<LocalDate> dates) {
        return registrations
                .entrySet()
                .stream()
                .filter(entry -> projectIds.contains(entry.getKey().projectId())
                                 && dates.contains(entry.getKey().date()))
                .reduce(Duration.ZERO,
                        (duration, entry) -> duration.plus(entry.getValue()),
                        Duration::plus);
    }

    private record ProjectDate(int projectId,
                               LocalDate date) {
    }
}
