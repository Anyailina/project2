package org.example.project2.converter;

import org.example.project2.model.dto.PersonDto;
import org.example.project2.model.entity.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonConverter {

    public PersonDto convert(Person person) {
        return new PersonDto().setId(person.getId())
                .setName(person.getName())
                .setSurname(person.getSurname())
                .setHeight(person.getHeight())
                .setAge(person.getAge());
    }

    public Person convert(PersonDto person) {
        return new Person().setId(person.getId())
                .setName(person.getName())
                .setSurname(person.getSurname())
                .setHeight(person.getHeight())
                .setAge(person.getAge());
    }
}
