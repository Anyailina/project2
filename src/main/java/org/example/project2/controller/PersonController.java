package org.example.project2.controller;


import org.example.project2.exception.EntityNotFoundException;
import org.example.project2.model.Person;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.repository.PersonRepo;
import org.example.project2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<PersonDto> getAllPerson() {
        return personService.getAllPerson();
    }

    @GetMapping("/{id}")
    public PersonDto getPersonsById(@PathVariable Long id) throws EntityNotFoundException {
        return personService.getPersonsById(id);
    }

    @PostMapping("/")
    public PersonDto addPerson(@RequestBody PersonDto person) {
        return personService.addPerson(person);
    }

    @PutMapping("/{id}")
    public PersonDto updatePersonById(@PathVariable Long id, @RequestBody PersonDto newPerson) throws EntityNotFoundException {
        return personService.updatePersonById(id,newPerson);
    }

    @DeleteMapping("/{id}")
    public  void deletePersonByID(@PathVariable Long id) {
        personService.deletePersonByID(id);
    }

}
