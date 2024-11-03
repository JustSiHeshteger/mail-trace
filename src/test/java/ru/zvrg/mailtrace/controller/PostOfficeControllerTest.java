package ru.zvrg.mailtrace.controller;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.util.TestUtils;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zvrg.mailtrace.common.consts.ExceptionConst.ADDRESS_ID_NOT_FOUND;
import static ru.zvrg.mailtrace.common.consts.ExceptionConst.POST_OFFICE_ID_NOT_FOUND;

@Transactional
@Sql(scripts = "/sql/data.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class PostOfficeControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/api/postOffice/";
    private final String BASE_DTO_PATH = "src/test/resources/dto/post-office/";
    private final String BASE_EXPECTED_DTO_PATH = BASE_DTO_PATH + "expected/";

    @Test
    @SneakyThrows
    void getAllPostOfficesTest() {
        var result = this.mockMvc.perform(get(BASE_URL + "getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        var expectedPostOfficeListDto = TestUtils.convertJsonFromFileToList(BASE_EXPECTED_DTO_PATH + "post-office-list-expected-dto.json", PostOfficeDto.class);
        assertThatJson(result.getResponse().getContentAsString())
                .isEqualTo(expectedPostOfficeListDto);
    }

    @Test
    @SneakyThrows
    void createNewPostOfficeTest() {
        var postOfficeDto = TestUtils.convertJsonFromFileToString(BASE_DTO_PATH + "post-office-create-dto.json", PostOfficeDto.class);

        var result = this.mockMvc.perform(post(BASE_URL + "createPostOffice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postOfficeDto))
                .andExpect(status().isOk())
                .andReturn();

        var actualPostOfficeDto = result.getResponse().getContentAsString();
        var expectedPostOfficeDto = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "post-office-create-expected-dto.json", PostOfficeDto.class);

        assertThatJson(actualPostOfficeDto)
                .isEqualTo(expectedPostOfficeDto);
    }

    @Test
    @SneakyThrows
    void updatePostOfficeWithExistingAddressTest() {
        var postOfficeDto = TestUtils.convertJsonFromFileToString(BASE_DTO_PATH + "post-office-update-dto.json", PostOfficeDto.class);

        var result = this.mockMvc.perform(put(BASE_URL + "updatePostOffice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postOfficeDto))
                .andExpect(status().isOk())
                .andReturn();

        var actualPostOfficeDto = result.getResponse().getContentAsString();
        var expectedPostOfficeDto = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "post-office-update-expected-dto.json", PostOfficeDto.class);

        assertThatJson(actualPostOfficeDto)
                .isEqualTo(expectedPostOfficeDto);
    }

    @Test
    @SneakyThrows
    void updatePostOfficeWithoutExistingAddressTest() {
        var postOfficeDto = TestUtils.convertJsonFromFileToString(BASE_DTO_PATH + "post-office-update-without-existing-address-dto.json", PostOfficeDto.class);

        var result = this.mockMvc.perform(put(BASE_URL + "updatePostOffice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postOfficeDto))
                .andExpect(status().isOk())
                .andReturn();

        var actualPostOfficeDto = result.getResponse().getContentAsString();
        var expectedPostOfficeDto = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "post-office-update-without-existing-address-expected-dto.json", PostOfficeDto.class);

        assertThatJson(actualPostOfficeDto)
                .whenIgnoringPaths("address.id")
                .isEqualTo(expectedPostOfficeDto);
    }

    @Test
    @SneakyThrows
    void updatePostOfficeWithExistingAddressExceptionTest() {
        var postOfficeDto = TestUtils.convertJsonFromFileToObject(BASE_DTO_PATH + "post-office-update-dto.json", PostOfficeDto.class);
        postOfficeDto.getAddress().setId(100000L);
        var postOfficeDtoString = TestUtils.convertJsonFromObjectToString(postOfficeDto);

        var result = this.mockMvc.perform(put(BASE_URL + "updatePostOffice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postOfficeDtoString))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(ADDRESS_ID_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void deletePostOfficeByIdTest() {
        Long id = 1L;
        this.mockMvc.perform(delete(BASE_URL + "deletePostOffice")
                        .param("postOfficeId", Long.toString(id)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deletePostOfficeByIdExceptionTest() {
        Long id = 100000L;
        var result = this.mockMvc.perform(delete(BASE_URL + "deletePostOffice")
                        .param("postOfficeId", Long.toString(id)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(POST_OFFICE_ID_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void searchPostOfficeByNameTest() {
        String term = "sa";
        var result = this.mockMvc.perform(get(BASE_URL + "searchByTerm")
                        .param("searchTerm", term))
                .andExpect(status().isOk())
                .andReturn();

        var actual = result.getResponse().getContentAsString();
        var expected = TestUtils.convertJsonFromFileToStringList(BASE_EXPECTED_DTO_PATH + "post-office-search-by-name-expected-list-dto.json", PostOfficeDto.class);

        assertNotNull(actual);
        assertThatJson(actual)
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void searchPostOfficeByPostalCodeTest() {
        String term = "1";
        var result = this.mockMvc.perform(get(BASE_URL + "searchByTerm")
                        .param("searchTerm", term))
                .andExpect(status().isOk())
                .andReturn();

        var actual = result.getResponse().getContentAsString();
        var expected = TestUtils.convertJsonFromFileToStringList(BASE_EXPECTED_DTO_PATH + "post-office-search-by-postal-code-expected-list-dto.json", PostOfficeDto.class);

        assertNotNull(actual);
        assertThatJson(actual)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(expected);
    }

}
