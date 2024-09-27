package org.example.project2.service;

import org.example.project2.converter.SchoolConverter;
import org.example.project2.exception.impl.IdNotCorrectException;
import org.example.project2.model.dto.SchoolDto;
import org.example.project2.model.entity.School;
import org.example.project2.repository.SchoolRepository;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolConverter schoolConverter;

    public SchoolService(SchoolRepository schoolRepository, SchoolConverter schoolConverter) {
        this.schoolRepository = schoolRepository;
        this.schoolConverter = schoolConverter;
    }

    public SchoolDto addSchool(SchoolDto schoolDto) {
        School school = schoolConverter.convert(schoolDto);
        School savedSchool = schoolRepository.save(school);
        schoolDto.setId(savedSchool.getId());
        return schoolDto;
    }

    public void deleteSchool(Long id) {
        School school = schoolRepository.findById(id).orElseThrow(IdNotCorrectException::new);
        if (!school.getDeleted()) {
            schoolRepository.deleteById(id);
        } else {
            throw new IdNotCorrectException();
        }
    }
}
