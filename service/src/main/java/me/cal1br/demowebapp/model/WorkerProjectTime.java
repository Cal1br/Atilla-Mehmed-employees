package me.cal1br.demowebapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class WorkerProjectTime {
    private long employeeId;
    private long projectId;
    private Date dateFrom;
    private Date dateTo;
}
