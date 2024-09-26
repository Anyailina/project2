package org.example.project2.controller;

import jakarta.validation.Valid;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public PersonDto getPersonsById(@PathVariable Long id) {
        return personService.getPersonsById(id);
    }

    @PostMapping("/")
    public PersonDto addPerson(@Valid @RequestBody PersonDto person) {
        return personService.addPerson(person);
    }

    @PutMapping("/{id}")
    public PersonDto updatePersonById(@PathVariable Long id, @RequestBody PersonDto newPerson) {
        return personService.updatePersonById(id, newPerson);
    }

    @DeleteMapping("/{id}")
    public void deletePersonByID(@PathVariable Long id) {
        personService.deletePersonByID(id);
    }

}
