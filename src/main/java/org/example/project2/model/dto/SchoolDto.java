package org.example.project2.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SchoolDto {
    private Long id;
    @NotEmpty
    @Size(min = 2)
    private String name;
    @NotEmpty
    @Size(min = 2)
    private String city;
    private Boolean deleted;
}
