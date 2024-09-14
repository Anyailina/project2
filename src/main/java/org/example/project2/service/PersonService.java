package org.example.project2.service;

import org.example.project2.PersonConverter;
import org.example.project2.exception.EntityNotFoundException;
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


    public PersonDto getPersonsById(Long id) throws EntityNotFoundException {
        return personRepo.findById(id)
                .map(personConverter::convert)
                .orElseThrow(EntityNotFoundException::new);
    }


    public PersonDto addPerson(PersonDto personDto) {
        Person person = personConverter.convert(personDto);
        personRepo.save(person);
        return personDto;
    }


    public PersonDto updatePersonById(Long id, PersonDto personDto)  throws EntityNotFoundException {
        return personRepo.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(personDto.getName());
                    existingPerson.setAge(personDto.getAge());
                    personRepo.save(existingPerson);
                    return personDto;
                })
                .orElseThrow(EntityNotFoundException::new);
    }


    public void deletePersonByID(Long id) {
        personRepo.deleteById(id);

    }
}

