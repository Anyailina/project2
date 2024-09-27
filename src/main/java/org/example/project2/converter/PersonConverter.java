package org.example.project2.converter;

import org.example.project2.model.dto.PersonDto;
import org.example.project2.model.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter {

    private final SchoolConverter schoolConverter;

    public PersonConverter(SchoolConverter schoolConverter) {
        this.schoolConverter = schoolConverter;
    }

    public PersonDto convert(Person person) {
        return new PersonDto().setId(person.getId())
                .setName(person.getName())
                .setSurname(person.getSurname())
                .setHeight(person.getHeight())
                .setAge(person.getAge())
                .setSchool(schoolConverter.convert(person.getSchool()));
    }

    public Person convert(PersonDto person) {
        return new Person().setId(person.getId())
                .setName(person.getName())
                .setSurname(person.getSurname())
                .setHeight(person.getHeight())
                .setAge(person.getAge())
                .setSchool(schoolConverter.convert(person.getSchool()));
    }
}
