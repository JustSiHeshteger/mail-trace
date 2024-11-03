package ru.zvrg.mailtrace.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.service.PostOfficeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postOffice")
public class PostOfficeController {

    private final PostOfficeService postOfficeService;

    @GetMapping("/getAll")
    public List<PostOfficeDto> getPostOfficeList() {
        return postOfficeService.findAllPostOffices();
    }

    @PostMapping("/createPostOffice")
    public PostOfficeDto createNewPostOffice(
            @RequestBody @Validated PostOfficeDto postOfficeDto) {

        return postOfficeService.savePostOffice(postOfficeDto);
    }

    @GetMapping("/searchByTerm")
    public List<PostOfficeDto> searchPostOffices(
            @RequestParam(value = "searchTerm") @NotBlank String searchTerm) {

        return postOfficeService.searchPostOfficesByTerm(searchTerm);
    }

    @PutMapping("/updatePostOffice")
    public PostOfficeDto updatePostOfficeById(
            @RequestBody @Validated PostOfficeDto postOfficeDto) {

        return postOfficeService.savePostOffice(postOfficeDto);
    }

    @DeleteMapping("/deletePostOffice")
    public ResponseEntity<Void> deletePostOffice(
            @RequestParam(value = "postOfficeId") @NotNull Long postOfficeId) {

        postOfficeService.deletePostOfficeById(postOfficeId);
        return ResponseEntity.ok().build();
    }
}
