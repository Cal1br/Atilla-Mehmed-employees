package me.cal1br.demowebapp.service;

import lombok.EqualsAndHashCode;
import me.cal1br.demowebapp.model.ProjectPairDuration;
import me.cal1br.demowebapp.model.WorkerProjectTime;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<ProjectPairDuration> calculate(final List<WorkerProjectTime> projectTimeList) {

        if (CollectionUtils.isEmpty(projectTimeList)) {
            return Collections.emptyList();
        }

        final Map<Long, List<WorkerProjectTime>> perProjectMap = new HashMap<>();
        for (final WorkerProjectTime workerProjectTime : projectTimeList) {
            perProjectMap.computeIfAbsent(workerProjectTime.getProjectId(), k -> new LinkedList<>())
                    .add(workerProjectTime);
        }

        final List<ProjectPairDuration> result = new LinkedList<>();

        for (final Map.Entry<Long, List<WorkerProjectTime>> longListEntry : perProjectMap.entrySet()) {
            int counter = 0;
            final Map<Set<Long>, Duration> mapPairDuration = new HashMap<>();

            //using iterators, because they are better for linked list than using List::get
            for (final WorkerProjectTime outerWorkPrj : longListEntry.getValue()) {
                final Iterator<WorkerProjectTime> inner = longListEntry.getValue().listIterator(counter);
                while (inner.hasNext()) {
                    final WorkerProjectTime innerWorkPrj = inner.next();
                    if (outerWorkPrj.getEmployeeId() == innerWorkPrj.getEmployeeId()) {
                        continue;
                    }
                    final Duration intersectingTime = getIntersectingTime(innerWorkPrj, outerWorkPrj);
                    if (!intersectingTime.isZero()) {
                        final Set<Long> key = Set.of(
                                innerWorkPrj.getEmployeeId(),
                                outerWorkPrj.getEmployeeId()
                        );
                        mapPairDuration.putIfAbsent(key, Duration.ZERO);
                        mapPairDuration.put(key, mapPairDuration.get(key).plus(intersectingTime));
                    }
                }
                counter++;
            }

            for (final Map.Entry<Set<Long>, Duration> setDurationEntry : mapPairDuration.entrySet()) {
                final ProjectPairDuration projectPairDuration = new ProjectPairDuration();
                final Iterator<Long> iterator = setDurationEntry.getKey().iterator();
                projectPairDuration.setEmployeeFirstId(iterator.next());
                projectPairDuration.setEmployeeSecondId(iterator.next());
                if (projectPairDuration.getEmployeeFirstId() > projectPairDuration.getEmployeeSecondId()) {
                    //ordering in a descending way, so we always get the same results
                    final long temp = projectPairDuration.getEmployeeFirstId();
                    projectPairDuration.setEmployeeFirstId(projectPairDuration.getEmployeeSecondId());
                    projectPairDuration.setEmployeeSecondId(temp);
                }
                projectPairDuration.setProjectId(longListEntry.getKey());
                projectPairDuration.setWorkedTogetherTime(setDurationEntry.getValue());
                result.add(projectPairDuration);
            }
        }

        //group per project
        //check if they have overlapping times
        //check the duration if they do
        //the algorithm should work if these durations are seperated

        return result.stream()
                .sorted(Comparator.comparing(ProjectPairDuration::getWorkedTogetherTime))
                .collect(Collectors.toList());
    }

    /**
     * Calculates and returns the amount of intersecting time
     *
     * @return the amount if there is an intersection, or {@link Duration} of Zero if there isn't any intersection
     */
    private Duration getIntersectingTime(final WorkerProjectTime innerWorkPrj, final WorkerProjectTime outerWorkPrj) {
        var from = getMax(innerWorkPrj.getDateFrom(), outerWorkPrj.getDateFrom());
        var to = getMin(innerWorkPrj.getDateTo(), outerWorkPrj.getDateTo());
        if (from.before(to)) {
           return Duration.between(from.toInstant(), to.toInstant());
        }
        return Duration.ZERO;
    }

    private <T extends Comparable<T>> T getMin(T obj1, T obj2) {
        final int result = obj1.compareTo(obj2);
        if (result < 0) {
            return obj1;
        } else {
            return obj2;
        }
    }

    private <T extends Comparable<T>> T getMax(T obj1, T obj2) {
        final int result = obj1.compareTo(obj2);
        if (result > 0) {
            return obj1;
        } else {
            return obj2;
        }
    }
}
