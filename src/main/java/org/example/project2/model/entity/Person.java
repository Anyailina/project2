package org.example.project2.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;


@Entity
@Table(name = "Persons")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private Integer height;
}
