package ru.zvrg.mailtrace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zvrg.mailtrace.dto.PostageDto;
import ru.zvrg.mailtrace.dto.TrackingDto;
import ru.zvrg.mailtrace.service.PostageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postage")
public class PostageController {

    private final PostageService postageService;

    /**
     * Создание новой посылки
     * @param postageDto посылка
     * @param postOfficeId почтовое отделение, в котором создается посылка
     */
    @PostMapping("/createPostage")
    public PostageDto createNewPostage(
            @RequestBody @Validated PostageDto postageDto,
            @RequestParam(value = "postOfficeId") Long postOfficeId) {

        return postageService.createNewPostage(postageDto, postOfficeId);
    }

    /**
     * Поиск посылки по id
     * @param identifier id послки
     */
    @GetMapping("/getPostage")
    public PostageDto getPostageById(
            @RequestParam(value = "postageId") UUID identifier) {

        return postageService.findPostageDtoById(identifier);
    }

    /**
     * Регистрация посылки в промежуточном почтовом отделении
     * @param postageId id посылки
     * @param postOfficeId id почтового отделения
     */
    @PutMapping("/registrationPostage")
    public PostageDto registrationPostageOnPostOffice(
            @RequestParam(value = "postageId") UUID postageId,
            @RequestParam(value = "postOffice") Long postOfficeId) {

        return postageService.registerPostageOnPostOffice(postageId, postOfficeId);
    }

    /**
     * Поиск маршрута посылки
     * @param postageId id посылки
     */
    @PostMapping("/postageTracking")
    public List<TrackingDto> getTrackingListByPostageId(
            @RequestParam(value = "postageId") UUID postageId) {

        return postageService.findTrackingDtoListByPostageId(postageId);
    }

    /**
     * Обновление посылки
     * @param postageDto посылка
     */
    @PostMapping("/updatePostage")
    public PostageDto updatePostage(
            @RequestBody @Validated PostageDto postageDto) {

        return postageService.updatePostage(postageDto);
    }

    /**
     * Удаление посылки из БД
     * @param postageId id посылки
     */
    @DeleteMapping("/deletePostage")
    public ResponseEntity<Void> deletePostageById(
            @RequestParam(value = "postageId") UUID postageId) {

        postageService.deletePostageByPostageId(postageId);
        return ResponseEntity.ok().build();
    }
}
