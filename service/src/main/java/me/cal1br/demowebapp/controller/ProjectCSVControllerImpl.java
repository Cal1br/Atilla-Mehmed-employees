package me.cal1br.demowebapp.controller;

import me.cal1br.demowebapp.model.ProjectPairDuration;
import me.cal1br.demowebapp.model.WorkerProjectTime;
import me.cal1br.demowebapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller

public class ProjectCSVControllerImpl implements ProjectCSVController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCSVControllerImpl.class);
    private final ProjectService service;
    private final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ProjectCSVControllerImpl(final ProjectService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<ProjectPairDuration>> uploadCSV(@RequestParam("file") final MultipartFile csv) {
        if (csv == null) {
            throw new IllegalArgumentException("No filed provided");
        }
        if (!"text/csv".equals(csv.getContentType())) {
            throw new IllegalArgumentException("Wrong content type");
        }
        try (final BufferedReader inputStream = new BufferedReader(new InputStreamReader(csv.getInputStream()))) {

            final List<WorkerProjectTime> list = new LinkedList<>();
            String line;
            //open csv had trouble paring longs, so I decided to do it manually. Normally I would never do this in a production environment.
            //later i figured out it was something wrong with the file, the string "147" was displayed, but the bytes were wrong
            while ((line = inputStream.readLine()) != null) {
                final String[] split = line.split(",");
                final WorkerProjectTime workerProjectTime = new WorkerProjectTime();
                try {
                    workerProjectTime.setDateFrom(parser.parse(split[2]));
                } catch (ParseException e) {
                    LOGGER.error("Couldn't parse date at line: {}", line);
                    throw new IllegalArgumentException("Could not parse date: " + split[2]);
                }
                if (split.length != 4) {
                    workerProjectTime.setDateTo(Date.from(Instant.now()));
                } else {
                    try {
                        workerProjectTime.setDateTo(parser.parse(split[3]));
                    } catch (ParseException e) {
                        workerProjectTime.setDateTo(Date.from(Instant.now()));
                    }
                }
                list.add(workerProjectTime);
            }

            return ResponseEntity.ok(service.calculate(list));

        } catch (IOException ioException) {
            LOGGER.error("Failed reading file {}, cause: {}", csv.getName(), ioException.getMessage());
            throw new RuntimeException("IO error");
        }
    }
}
