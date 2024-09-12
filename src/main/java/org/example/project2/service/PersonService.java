package org.example.project2.service;

import org.example.project2.PersonConverter;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.model.Person;
import org.example.project2.repository.PersonRepo;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepo personRepo;
    private final PersonConverter personConverter;

    public PersonService(PersonRepo personRepo, PersonConverter personConverter) {
        this.personRepo = personRepo;
        this.personConverter = personConverter;
    }

    public List<PersonDto> getAllPerson() {
        List<Person> personList = personRepo.findAll();

        if (personList.isEmpty()) {
            return new ArrayList<>();
        }

        return personList.stream().map(personConverter::convert).toList();
    }


    public PersonDto getPersonsById(Long id) {
        return personRepo.findById(id)
                .map(personConverter::convert)
                .orElseGet(null);
    }


    public PersonDto addPerson(Person person) {
        Person personObj = personRepo.save(person);
        return personConverter.convert(person);
    }


    public PersonDto updatePersonById(Long id, Person newPerson) {
        return personRepo.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(newPerson.getName());
                    existingPerson.setAge(newPerson.getAge());
                    personRepo.save(existingPerson);
                    return personConverter.convert(existingPerson);
                })
                .orElseGet(null);
    }


    public void deletePersonByID(Long id) {
        personRepo.deleteById(id);

    }
}

