package org.example.project2;

import org.example.project2.configuration.TestConfiguration;
import org.example.project2.model.entity.School;
import org.example.project2.repository.SchoolRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(value = {
        TestConfiguration.class
})
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class SchoolApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SchoolRepository schoolRepository;

    @AfterEach
    void tearDown() {
        schoolRepository.deleteAll();
    }

    @Test
    void addSchoolTest(@Value("classpath:stub/school/addSchoolDtoRequest.json") Resource request) throws Exception {
        String requestSchoolDto = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(requestSchoolDto);

        MvcResult result = mockMvc.perform(post("/school/")
                        .content(requestSchoolDto)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String actualPersonDto = result.getResponse().getContentAsString();
        System.out.println(actualPersonDto);
        JSONAssert.assertEquals(requestSchoolDto, actualPersonDto, new CustomComparator(STRICT, new Customization("id", (it1, it2) -> true)));
    }

    @Test
    void addSchoolWithWrongBody(@Value("classpath:stub/school/addSchoolDtoBadRequest.json") Resource request) throws Exception {
        String schoolWithWrongBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        mockMvc.perform(post("/school/")
                        .content(schoolWithWrongBody)
                        .contentType(MediaType.APPLICATION_JSON.getMediaType()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteSchool() throws Exception {
        School savedSchool = createSchool(false);
        Optional<School> actualSchoolOptional = schoolRepository.findById(savedSchool.getId());
        Assertions.assertTrue(actualSchoolOptional.isPresent(), "School not found");
        School actualSchool = actualSchoolOptional.get();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(savedSchool), objectMapper.writeValueAsString(actualSchool), new CustomComparator(STRICT));

        mockMvc.perform(delete("/school/" + savedSchool.getId()))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertTrue(schoolRepository.findById(savedSchool.getId()).isEmpty());
    }

    @Test
    void deleteSchoolIfIsDeleted() throws Exception {
        School savedSchool = createSchool(true);
        System.out.println(savedSchool);
        mockMvc.perform(delete("/school/" + savedSchool.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteSchoolWithWrongId() throws Exception {
        mockMvc.perform(delete("/school/-7")).andExpect(status().is4xxClientError());
    }

    private School createSchool(boolean deleted) {
        School school = new School(1L, "kfds", "dklsd", deleted);
        return schoolRepository.save(school);
    }
}
