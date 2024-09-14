package org.example.project2;

import org.example.project2.model.Person;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.repository.PersonRepo;
import org.example.project2.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
@SpringJUnitConfig

@Import({PersonService.class, PersonConverter.class})
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepo.deleteAll();  // Очищаем базу данных перед каждым тестом
    }


    @Test
    void testGetAllPersons() {
        // Given
        Person person1 = new Person(1L, "John", "Ragimov",76,185);
        Person person2 = new Person(2L, "Petr", "Ragimov",76,156);

        personRepo.save(person1);
        personRepo.save(person2);

        List<PersonDto> personDtos = personService.getAllPerson();

//        Assertions.assertEquals(2, personDtos.size());
//        Assertions.assertEquals("John", personDtos.get(0).getName());
//        Assertions.assertEquals(76, personDtos.get(0).getAge());
//        Assertions.assertEquals("Petr", personDtos.get(1).getName());
//        Assertions.assertEquals(156, personDtos.get(1).getHeight());

    }
}
