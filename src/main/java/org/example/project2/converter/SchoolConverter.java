package org.example.project2.converter;

import org.example.project2.model.dto.SchoolDto;
import org.example.project2.model.entity.School;
import org.springframework.stereotype.Component;

@Component
public class SchoolConverter {
    public SchoolDto convert(School school) {
        return new SchoolDto().setId(school.getId())
                .setName(school.getName())
                .setCity(school.getCity());
    }

    public School convert(SchoolDto schoolDto) {
        return new School().setId(schoolDto.getId())
                .setName(schoolDto.getName())
                .setCity(schoolDto.getCity());
    }
}
