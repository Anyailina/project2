package org.example.project2;

import org.example.project2.model.Person;
import org.example.project2.model.dto.PersonDto;
import org.example.project2.repository.PersonRepo;
import org.example.project2.service.PersonService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class PersonServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonConverter personConverter;
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Person person = new Person(1L, "John", "JKDF", 30, 167);
    private final Person personWithWrongBody = new Person(null, "", "jfksld", 30, 5);
    private final PersonDto personDto = new PersonDto(1L, "Anna", "JKHJBF", 12, 176);

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void getAllPerson() throws Exception {
        mockMvc.perform(get("http://localhost:8080/person/"))
                .andExpect(status().is2xxSuccessful());
        personRepo.save(person);

        Assertions.assertEquals(1, personRepo.findAll().size());
        Assertions.assertEquals(1, personService.getAllPerson().size());
    }

    @Test
    void getPersonsByIdTest() throws Exception {
        personRepo.save(person);
        MvcResult result = mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andReturn();

        Person actualPerson = personDtoToPerson(result);

        assertThat(actualPerson)
                .isEqualToComparingFieldByFieldRecursively(person);
    }

    @Test
    void getPersonByWrongIdTest() throws Exception {
        mockMvc.perform(get("/person/-1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addPersonTest() throws Exception {
        PersonDto personDto = personConverter.convert(person);
        personRepo.save(person);

        MvcResult result = mockMvc.perform(post("/person/")
                        .content(objectMapper.writeValueAsString(personDto))
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Person actualPerson = personDtoToPerson(result);
        assertThat(actualPerson)
                .isEqualToComparingFieldByFieldRecursively(person);
    }

    @Test
    void addPersonWithWrongBody() throws Exception {
        PersonDto personDto = personConverter.convert(personWithWrongBody);

        mockMvc.perform(post("/person/")
                        .content(objectMapper.writeValueAsString(personDto))
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePersonTest() throws Exception {
        personRepo.save(person);

        MvcResult result = mockMvc.perform(put("/person/1")
                        .content(objectMapper.writeValueAsString(personDto))
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Optional<Person> actualPersonOptional = personRepo.findById(1L);
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = personRepo.findById(1L).get();
        String content = result.getResponse().getContentAsString();
        PersonDto expectedPersonDto = objectMapper.readValue(content, PersonDto.class);


        assertThat(actualPerson)
                .isEqualToComparingFieldByFieldRecursively(expectedPersonDto);
    }

    @Test
    void updateWithWrongPersonTest() throws Exception {
        PersonDto personDtoWrongBody = new PersonDto(null, "", "jfksld", 30, 5);
        personRepo.save(person);

        mockMvc.perform(put("/person/")
                        .content(objectMapper.writeValueAsString(personDtoWrongBody))
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateWithWrongIdTest() throws Exception {
        personRepo.save(person);

        mockMvc.perform(put("/person/-1")
                        .content(objectMapper.writeValueAsString(personDto))
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void deletePerson() throws Exception {
        personRepo.save(person);

        Optional<Person> actualPersonOptional = personRepo.findById(1L);
        Assertions.assertTrue(actualPersonOptional.isPresent(), "Person not found");
        Person actualPerson = actualPersonOptional.get();

        assertThat(actualPerson)
                .isEqualToComparingFieldByFieldRecursively(person);

        mockMvc.perform(delete("/person/1", person))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertTrue(personRepo.findById(1L).isEmpty());
    }

    @Test
    void deletePersonWithWrongId() throws Exception {
        mockMvc.perform(delete("/person/-7")).andExpect(status().is2xxSuccessful());
    }

    private Person personDtoToPerson(MvcResult result) throws IOException {
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, Person.class);
    }
}

