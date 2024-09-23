package org.example.project2.model.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDto {
    @NotNull
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 10)
    private String name;
    @NotEmpty
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
