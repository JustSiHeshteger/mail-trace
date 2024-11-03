package ru.zvrg.mailtrace.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zvrg.mailtrace.common.exception.MailTraceException;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.entity.PostOffice;
import ru.zvrg.mailtrace.mapper.PostOfficeMapper;
import ru.zvrg.mailtrace.repository.PostOfficeRepository;

import java.util.List;
import java.util.Objects;

import static ru.zvrg.mailtrace.common.consts.ExceptionConst.POST_OFFICE_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostOfficeService {

    private final PostOfficeRepository postOfficeRepository;
    private final PostOfficeMapper postOfficeMapper;
    private final AddressService addressService;

    public PostOffice findPostOfficeById(@NotNull Long id) {
        return postOfficeRepository.findById(id)
                .orElseThrow(() -> new MailTraceException(POST_OFFICE_ID_NOT_FOUND));
    }

    public List<PostOfficeDto> findAllPostOffices() {
        return postOfficeMapper.toDtoList(postOfficeRepository.findAll());
    }

    @Transactional
    public PostOfficeDto savePostOffice(PostOfficeDto postOfficeDto) {
        var postOffice = postOfficeMapper.toEntity(postOfficeDto);

        if (Objects.nonNull(postOffice.getAddress()) && Objects.nonNull(postOffice.getAddress().getId())) {
            var existingAddress = addressService.getAddressById(postOffice.getAddress().getId());
            postOffice.setAddress(existingAddress);
        }

        var savedPostOfficeDto = postOfficeRepository.save(postOffice);
        return postOfficeMapper.toDto(savedPostOfficeDto);
    }

    public List<PostOfficeDto> searchPostOfficesByTerm(@NotBlank String searchTerm) {
        return postOfficeMapper.toDtoList(postOfficeRepository.findPostOfficeByTermContaining(searchTerm));
    }

    @Transactional
    public void deletePostOfficeById(@NotNull Long postOfficeId) {
        if (!postOfficeRepository.existsById(postOfficeId)) {
            throw new MailTraceException(POST_OFFICE_ID_NOT_FOUND);
        }

        postOfficeRepository.deleteById(postOfficeId);
    }
}
