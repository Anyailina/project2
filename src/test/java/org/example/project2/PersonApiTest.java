package org.example.project2;


import org.example.project2.configuration.TestConfiguration;
import org.example.project2.converter.PersonConverter;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.model.entity.Person;
import org.example.project2.model.entity.School;
import org.example.project2.repository.PersonRepository;
import org.example.project2.repository.SchoolRepository;
import org.example.project2.service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(value = {
        TestConfiguration.class
})
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class PersonApiTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonConverter personConverter;
    @Autowired
    private SchoolRepository schoolRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void getAllPerson() throws Exception {
        System.out.println("fklsd");
        createPerson();
        mockMvc.perform(get("/person/"))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(1, personRepository.findAll().size());
        Assertions.assertEquals(1, personService.getAllPerson().size());
    }

    @Test
    void getPersonsByIdTest() throws Exception {
        Person savedPerson = createPerson();
        PersonDto personDto = personConverter.convert(savedPerson);

        MvcResult result = mockMvc.perform(get("/person/" + savedPerson.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String actualPersonDto = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(objectMapper.writeValueAsString(personDto), actualPersonDto, new CustomComparator(STRICT));
    }

    @Test
    void getPersonByWrongIdTest() throws Exception {
        mockMvc.perform(get("/person/-1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addPersonTest(@Value("classpath:stub/addPersonDtoRequest.json") Resource request) throws Exception {
        String requestPersonDto = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        MvcResult result = mockMvc.perform(post("/person/")
                        .content(requestPersonDto)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String actualPersonDto = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(requestPersonDto, actualPersonDto, new CustomComparator(STRICT, new Customization("id", (it1, it2) -> true)));
    }

    @Test
    void addPersonWithWrongBody(@Value("classpath:stub/addPersonDtoBadRequest.json") Resource request) throws Exception {
        String personWithWrongBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        mockMvc.perform(post("/person/")
                        .content(personWithWrongBody)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updatePersonTest(@Value("classpath:stub/updatePersonDtoRequest.json") Resource request
    ) throws Exception {
        String requestPersonDto = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        Person person = createPerson();
        Person savedPerson = personRepository.save(person);
        long savedPersonId = savedPerson.getId();

        mockMvc.perform(put("/person/" + savedPersonId)
                        .content(requestPersonDto)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is2xxSuccessful());

        Optional<Person> actualPersonOptional = personRepository.findById(savedPersonId);
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = personRepository.findById(savedPersonId).get();
        PersonDto actualPersonDto = personConverter.convert(actualPerson);

        JSONAssert.assertEquals(requestPersonDto,
                objectMapper.writeValueAsString(actualPersonDto), new CustomComparator(JSONCompareMode.STRICT, new Customization("id", (it1, it2) -> true)));
    }

    @Test
    void updateWithWrongPersonTest(@Value("classpath:stub/updatePersonDtoBadRequest.json") Resource request) throws Exception {
        String personDto = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        mockMvc.perform(put("/person/")
                        .content(personDto)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateWithWrongIdTest(@Value("classpath:stub/updatePersonDtoRequest.json") Resource request) throws Exception {
        createPerson();
        String personDto = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        mockMvc.perform(put("/person/-1")
                        .content(personDto)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deletePerson() throws Exception {
        Person savedperson = createPerson();
        Optional<Person> actualPersonOptional = personRepository.findById(savedperson.getId());
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = actualPersonOptional.get();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(savedperson), objectMapper.writeValueAsString(actualPerson), new CustomComparator(STRICT));

        mockMvc.perform(delete("/person/" + savedperson.getId()))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertTrue(personRepository.findById(savedperson.getId()).isEmpty());
    }

    @Test
    void deletePersonWithWrongId() throws Exception {
        mockMvc.perform(delete("/person/-7")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void addPersonToSchool() throws Exception {
        Person person = createPerson();
        School school = new School(2L, "kclds;", "dsfmls", false);
        School savedSchool = schoolRepository.save(school);

        mockMvc.perform(put("/person/" + person.getId() + "/school/" + savedSchool.getId()))
                .andExpect(status().is2xxSuccessful()).andReturn();

        Optional<Person> actualPersonOptional = personRepository.findById(person.getId());
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = actualPersonOptional.get();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(actualPerson.getSchool()), objectMapper.writeValueAsString(savedSchool), new CustomComparator(STRICT));
    }

    @Test
    void addPersonToSchoolWithWrongId() throws Exception {
        Person person = createPerson();
        School school = new School(2L, "kclds;", "dsfmls", false);
        School savedSchool = schoolRepository.save(school);

        mockMvc.perform(put("/person/" + "-1" + "/school/" + savedSchool.getId()))
                .andExpect(status().is4xxClientError()).andReturn();

        mockMvc.perform(put("/person/" + person.getId() + "/school/" + "-1"))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    void deletePersonToSchoolWithWrongId() throws Exception {
        createPerson();

        mockMvc.perform(put("/person/deleteSchool/" + "-1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deletePersonToSchool() throws Exception {
        Person person = createPerson();
        person.getSchool().setDeleted(true);

        mockMvc.perform(delete("/person/deleteSchool/" + person.getId()))
                .andExpect(status().is2xxSuccessful());

        Optional<Person> actualPersonOptional = personRepository.findById(person.getId());
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = actualPersonOptional.get();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(person), objectMapper.writeValueAsString(actualPerson), new CustomComparator(STRICT));
    }

    private Person createPerson() {
        School savedSchool = createSchool();
        Person person = new Person(1L, "John", "JKDF", 30, 167, savedSchool);
        return personRepository.save(person);
    }

    private School createSchool() {
        School school = new School(1L, "kfds", "dklsd", false);
        return schoolRepository.save(school);
    }
}

