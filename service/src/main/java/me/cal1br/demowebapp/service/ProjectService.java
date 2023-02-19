package me.cal1br.demowebapp.service;

import me.cal1br.demowebapp.model.ProjectPairDuration;
import me.cal1br.demowebapp.model.WorkerProjectTime;

import java.util.List;

public interface ProjectService {
    List<ProjectPairDuration> calculate(List<WorkerProjectTime> projectTimeList);
}
