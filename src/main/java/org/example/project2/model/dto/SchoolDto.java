package org.example.project2.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SchoolDto {
    private Long id;
    private String name;
    private String city;
}
