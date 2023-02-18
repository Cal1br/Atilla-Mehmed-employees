package me.cal1br.demowebapp.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(
        path = "project"
)
public interface ProjectCSVController {

    @PostMapping("/upload")
    void uploadCSV(@RequestParam final MultipartFile csv);
}
