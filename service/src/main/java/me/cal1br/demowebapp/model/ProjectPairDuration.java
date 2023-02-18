package me.cal1br.demowebapp.model;

import lombok.Data;

import java.time.Duration;

/**
 * A model class containing two employees, and the time they have worked together on a given project
 */
@Data
public class ProjectPairDuration {
    private long employeeFirstId;
    private long employeeSecondId;
    private long projectId;
    private Duration workedTogetherTime;
}
