package org.example.project2.model.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PersonDto {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private Integer height;
}
