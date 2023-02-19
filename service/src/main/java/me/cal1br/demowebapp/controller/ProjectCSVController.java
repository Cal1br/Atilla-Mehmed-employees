package me.cal1br.demowebapp.controller;

import me.cal1br.demowebapp.model.ProjectPairDuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(
        path = "project"
)
public interface ProjectCSVController {

    @PostMapping("/upload")
    ResponseEntity<List<ProjectPairDuration>> uploadCSV(final MultipartFile csv);
}
