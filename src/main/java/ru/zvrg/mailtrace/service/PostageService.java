package ru.zvrg.mailtrace.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zvrg.mailtrace.common.PostageStatus;
import ru.zvrg.mailtrace.common.exception.MailTraceException;
import ru.zvrg.mailtrace.dto.PostageDto;
import ru.zvrg.mailtrace.dto.TrackingDto;
import ru.zvrg.mailtrace.entity.PostOffice;
import ru.zvrg.mailtrace.entity.Postage;
import ru.zvrg.mailtrace.entity.Tracking;
import ru.zvrg.mailtrace.mapper.PostageMapper;
import ru.zvrg.mailtrace.mapper.TrackingMapper;
import ru.zvrg.mailtrace.repository.PostageRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.zvrg.mailtrace.common.consts.ExceptionConst.POSTAGE_IDENTIFIER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostageService {

    private final PostageRepository postageRepository;
    private final PostOfficeService postOfficeService;
    private final TrackingMapper trackingMapper;
    private final PostageMapper postageMapper;

    public Postage findPostageById(@NotNull UUID postageId) {
        return postageRepository.findById(postageId)
                .orElseThrow(() -> new MailTraceException(POSTAGE_IDENTIFIER_NOT_FOUND));
    }

    @Transactional
    public PostageDto createNewPostage(PostageDto postageDto, @NotNull Long postOfficeId) {
        final var postOffice = postOfficeService.findPostOfficeById(postOfficeId);
        final var postage = postageMapper.createNewPostage(postageDto, postOffice);

        return postageMapper.toDto(postageRepository.save(postage));
    }

    public PostageDto findPostageDtoById(@NotNull UUID postageId) {
        final var postage = findPostageById(postageId);
        return postageMapper.toDto(postage);
    }

    @Transactional
    public PostageDto updatePostage(PostageDto postageDto) {
        final var updatedPostage = postageMapper.toEntity(postageDto);
        final var trackingList = findTrackingListByPostageId(postageDto.getIdentifier());

        trackingList.forEach(
                tracking -> tracking.setPostage(updatedPostage)
        );

        updatedPostage.setTrackingList(trackingList);
        final var savedPostage = postageRepository.save(updatedPostage);
        return postageMapper.toDto(savedPostage);
    }

    @Transactional
    public PostageDto registerPostageOnPostOffice(@NotNull UUID postageId, @NotNull Long postOfficeId) {
        final var postage = findPostageById(postageId);
        final var postOffice = postOfficeService.findPostOfficeById(postOfficeId);

        this.updatePostageStatus(postage, postOffice);
        postage.getTrackingList().add(trackingMapper.createNewTracking(postage, postOffice));

        return postageMapper.toDto(postageRepository.save(postage));
    }

    public List<TrackingDto> findTrackingDtoListByPostageId(@NotNull UUID postageId) {
        return trackingMapper.toDtoList(findTrackingListByPostageId(postageId));
    }

    @Transactional
    public void deletePostageByPostageId(@NotNull UUID postageId) {
        if (!postageRepository.existsById(postageId)) {
            throw new MailTraceException(POSTAGE_IDENTIFIER_NOT_FOUND);
        }

        postageRepository.deleteById(postageId);
    }

    private void updatePostageStatus(Postage postage, PostOffice postOffice) {
        final var postageAddress = postage.getPerson().getAddress();
        final var postOfficeAddress = postOffice.getAddress();

        if (!Objects.equals(postageAddress, postOfficeAddress)) {
            postage.setStatus(PostageStatus.AWAITING_PICK_UP);
        }
    }

    private List<Tracking> findTrackingListByPostageId(@NotNull UUID postageId) {
        final var postage = findPostageById(postageId);
        return postage.getTrackingList();
    }

}
