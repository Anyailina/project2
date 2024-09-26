package org.example.project2.service;

import org.example.project2.converter.PersonConverter;
import org.example.project2.exception.impl.EntityNotFoundException;
import org.example.project2.exception.impl.IdNotCorrectException;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.model.entity.Person;
import org.example.project2.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonConverter personConverter;

    public PersonService(PersonRepository personRepository, PersonConverter personConverter) {
        this.personRepository = personRepository;
        this.personConverter = personConverter;
    }

    public List<PersonDto> getAllPerson() {
        List<Person> personList = personRepository.findAll();

        if (personList.isEmpty()) {
            return new ArrayList<>();
        }

        return personList.stream().map(personConverter::convert).toList();
    }


    public PersonDto getPersonsById(Long id) throws IdNotCorrectException {
        return personRepository.findById(id)
                .map(personConverter::convert)
                .orElseThrow(IdNotCorrectException::new);
    }


    public PersonDto addPerson(PersonDto personDto) {
        Person person = personConverter.convert(personDto);
        personRepository.save(person);
        personDto.setId(person.getId());
        return personDto;
    }


    public PersonDto updatePersonById(Long id, PersonDto personDto) throws EntityNotFoundException {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(personDto.getName());
                    existingPerson.setAge(personDto.getAge());
                    existingPerson.setSurname(personDto.getSurname());
                    existingPerson.setHeight(personDto.getHeight());
                    personRepository.save(existingPerson);
                    return personDto;
                })
                .orElseThrow(IdNotCorrectException::new);
    }


    public void deletePersonByID(Long id) {
        personRepository.deleteById(id);
    }
}

