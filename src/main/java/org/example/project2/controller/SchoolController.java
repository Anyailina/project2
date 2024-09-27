package org.example.project2.controller;

import jakarta.validation.Valid;
import org.example.project2.model.dto.SchoolDto;
import org.example.project2.service.SchoolService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping("/")
    public SchoolDto addSchool(@Valid @RequestBody SchoolDto personDto) {
        return schoolService.addSchool(personDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
    }
}
