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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.zvrg.mailtrace.common.error.ValidateErrorList;
import ru.zvrg.mailtrace.common.exception.MailTraceException;
import ru.zvrg.mailtrace.dto.PostageDto;
import ru.zvrg.mailtrace.dto.TrackingDto;
import ru.zvrg.mailtrace.util.TestUtils;

import java.util.Objects;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zvrg.mailtrace.common.consts.ExceptionConst.POSTAGE_IDENTIFIER_NOT_FOUND;
import static ru.zvrg.mailtrace.common.consts.ExceptionConst.POST_OFFICE_ID_NOT_FOUND;

@Transactional
@Sql(scripts = "/sql/data.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class PostageControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private MockMvc mockMvc;

    private final String IDENTIFIER = "ea901f00-ecfe-4bfc-9b35-b9e0356d3e21";
    private final String BASE_URL = "/api/postage/";
    private final String BASE_ENTITY_PATH = "src/test/resources/entity/postage/";
    private final String BASE_DTO_PATH = "src/test/resources/dto/postage/";
    private final String BASE_EXPECTED_DTO_PATH = BASE_DTO_PATH + "expected/";
    private final String BASE_ERROR_PATH = "src/test/resources/error/validate/";

    @Test
    @SneakyThrows
    void createNewPostageTest() {
        var postageJson = TestUtils.convertJsonFromFileToString(BASE_ENTITY_PATH + "postage-entity.json", PostageDto.class);

        var result = this.mockMvc.perform(post(BASE_URL + "createPostage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postageJson)
                    .param("postOfficeId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualPostageDtoJson = result.getResponse().getContentAsString();
        String expectedPostageDtoJson = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "create-postage-expected-dto.json", PostageDto.class);

        assertThatJson(actualPostageDtoJson)
                .whenIgnoringPaths("trackingList[*].timestamp", "identifier", "trackingList[*].id")
                .isEqualTo(expectedPostageDtoJson);
    }

    @Test
    @SneakyThrows
    void createNewPostageValidateExceptionTest() {
        var postageJson = TestUtils.readJsonFromFileWithoutAttributes(BASE_ENTITY_PATH + "postage-entity.json", "postageType");

        var result = this.mockMvc.perform(post(BASE_URL + "createPostage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postageJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        var expectedErrorList = TestUtils.convertJsonFromFileToObject(BASE_ERROR_PATH + "validate-error.json", ValidateErrorList.class);
        var actualErrorList = TestUtils.convertJsonFromStringToObject(result.getResponse().getContentAsString(), ValidateErrorList.class);

        assertTrue(expectedErrorList.getValidateErrorList().containsAll(actualErrorList.getValidateErrorList()));
        assertTrue(actualErrorList.getValidateErrorList().containsAll(expectedErrorList.getValidateErrorList()));
        assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(), MethodArgumentNotValidException.class);
    }

    @Test
    @SneakyThrows
    void createNewPostageValidateMultipleExceptionTest() {
        var postageJson = TestUtils.readJsonFromFileWithoutAttributes(BASE_ENTITY_PATH + "postage-entity.json", "postageType", "firstName");

        var result = this.mockMvc.perform(post(BASE_URL + "createPostage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postageJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        var expectedErrorList = TestUtils.convertJsonFromFileToObject(BASE_ERROR_PATH + "validate-error-list.json", ValidateErrorList.class);
        var actualErrorList = TestUtils.convertJsonFromStringToObject(result.getResponse().getContentAsString(), ValidateErrorList.class);

        assertTrue(expectedErrorList.getValidateErrorList().containsAll(actualErrorList.getValidateErrorList()));
        assertTrue(actualErrorList.getValidateErrorList().containsAll(expectedErrorList.getValidateErrorList()));
        assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(), MethodArgumentNotValidException.class);
    }

    @Test
    @SneakyThrows
    void getPostageByIdTest() {
        var identifier = UUID.fromString(IDENTIFIER);

        var result = this.mockMvc.perform(get(BASE_URL + "getPostage")
                    .param("postageId", identifier.toString()))
                .andExpect(status().isOk())
                .andReturn();

        var expectedPostageDtoJson = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "postage-expected-dto.json", PostageDto.class);
        var actualPostageDtoJson = result.getResponse().getContentAsString();

        assertThatJson(actualPostageDtoJson)
                .whenIgnoringPaths("trackingList[*].timestamp")
                .isEqualTo(expectedPostageDtoJson);
    }

    @Test
    @SneakyThrows
    void getPostageByIdNotFoundExceptionTest() {
        var identifier = "ea901f00-ecfe-4bfc-9b35-b9e0356d3e24";

        var result = this.mockMvc.perform(get(BASE_URL + "getPostage")
                    .param("postageId", identifier.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(POSTAGE_IDENTIFIER_NOT_FOUND, result.getResponse().getContentAsString());
        assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(), MailTraceException.class);
    }

    @Test
    @SneakyThrows
    void getTrackingListByPostageIdTest() {
        var result = this.mockMvc.perform(post(BASE_URL + "postageTracking")
                    .param("postageId", IDENTIFIER))
                .andExpect(status().isOk())
                .andReturn();

        var expectedTrackingDtoList = TestUtils.convertJsonFromObjectToString(
                TestUtils.convertJsonFromFileToList(BASE_EXPECTED_DTO_PATH + "tracking-list-expected-dto.json", TrackingDto.class));
        var actualTrackingDtoList = result.getResponse().getContentAsString();

        assertThatJson(actualTrackingDtoList)
                .whenIgnoringPaths("[*].timestamp")
                .isEqualTo(expectedTrackingDtoList);
    }

    @Test
    @SneakyThrows
    void getTrackingListByPostageIdNotFoundExceptionTest() {
        var identifier = "ea901f00-ecfe-4bfc-9b35-b9e0356d3e24";

        var result = this.mockMvc.perform(post(BASE_URL + "postageTracking")
                    .param("postageId", identifier))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(POSTAGE_IDENTIFIER_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void deletePostageByPostageIdTest() {
        this.mockMvc.perform(delete(BASE_URL + "deletePostage")
                .param("postageId", IDENTIFIER))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deletePostageByPostageIdExceptionTest() {
        var identifier = "ea901f00-ecfe-4bfc-9b35-b9e0356d3e24";

        var result = this.mockMvc.perform(delete(BASE_URL + "deletePostage")
                .param("postageId", identifier))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(POSTAGE_IDENTIFIER_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void registrationPostageTest() {
        var postOfficeId = "2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("postageId", IDENTIFIER);
        params.add("postOffice", postOfficeId);

        var result = this.mockMvc.perform(put(BASE_URL + "registrationPostage")
                .params(params))
                .andExpect(status().isOk())
                .andReturn();

        var expectedPostageDto = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "registration-postage-expected-dto.json", PostageDto.class);
        assertThatJson(result.getResponse().getContentAsString())
                .whenIgnoringPaths("trackingList[*].timestamp")
                .isEqualTo(expectedPostageDto);
    }

    @Test
    @SneakyThrows
    void registrationPostageExceptionByPostOfficeTest() {
        var postOfficeId = "3";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("postageId", IDENTIFIER);
        params.add("postOffice", postOfficeId);

        var result = this.mockMvc.perform(put(BASE_URL + "registrationPostage")
                        .params(params))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(POST_OFFICE_ID_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void registrationPostageExceptionByPostageTest() {
        var identifier = "ea901f00-ecfe-4bfc-9b35-b9e0356d3e24";
        var postOfficeId = "2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("postageId", identifier);
        params.add("postOffice", postOfficeId);

        var result = this.mockMvc.perform(put(BASE_URL + "registrationPostage")
                        .params(params))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(POSTAGE_IDENTIFIER_NOT_FOUND, result.getResponse().getContentAsString());
    }

    @Test
    @SneakyThrows
    void updatePostageTest() {
        var postageJson = TestUtils.convertJsonFromFileToString(BASE_DTO_PATH + "postage-update-dto.json", PostageDto.class);

        var result = this.mockMvc.perform(post(BASE_URL + "updatePostage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postageJson))
                .andExpect(status().isOk())
                .andReturn();

        String actualPostageDtoJson = result.getResponse().getContentAsString();
        String expectedPostageDtoJson = TestUtils.convertJsonFromFileToString(BASE_EXPECTED_DTO_PATH + "postage-update-expected-dto.json", PostageDto.class);

        assertThatJson(actualPostageDtoJson)
                .whenIgnoringPaths("trackingList[*].timestamp")
                .isEqualTo(expectedPostageDtoJson);
    }
}
