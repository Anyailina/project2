package org.example.project2.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "schools")
@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String city;
    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<Person> persons;

    public School(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }
}
