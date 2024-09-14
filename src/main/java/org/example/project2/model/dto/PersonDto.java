package org.example.project2.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDto {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 2, max = 10)
    private String name;
    @NotNull
    @Size(min = 2, max = 10)
    private String surname;
    @NotNull
    @Min(value = 0)
    @Max(value = 120)
    private Integer age;
    @NotNull
    @Min(value = 150)
    @Max(value = 230)
    private Integer height;
}
