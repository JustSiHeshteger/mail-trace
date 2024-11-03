package ru.zvrg.mailtrace.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zvrg.mailtrace.common.PostageStatus;
import ru.zvrg.mailtrace.dto.PostageDto;
import ru.zvrg.mailtrace.entity.PostOffice;
import ru.zvrg.mailtrace.entity.Postage;
import ru.zvrg.mailtrace.mapper.converter.PostageMapperImpl;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostageMapper extends PostageMapperImpl {

    private PersonMapper personMapper;
    private TrackingMapper trackingMapper;

    public Postage createNewPostage(@NotNull PostageDto postageDto, @NotNull PostOffice postOffice) {
        final var postageEntity = toEntity(postageDto);

        var postage = new Postage()
                .setIdentifier(Objects.nonNull(postageEntity.getIdentifier()) ? postageEntity.getIdentifier() : generateIdentifier())
                .setPostageType(postageEntity.getPostageType())
                .setStatus(PostageStatus.IN_TRANSIT)
                .setPerson(postageEntity.getPerson());

        postage.setTrackingList(Collections.singletonList(trackingMapper.createNewTracking(postage, postOffice)));

        return postage;
    }

    private UUID generateIdentifier() {
        return UUID.randomUUID();
    }

    @Override
    public Postage toEntity(PostageDto postageDto) {
        if (postageDto == null) {
            return null;
        }

        Postage postage = new Postage();

        postage.setIdentifier(postageDto.getIdentifier());
        postage.setPostageType(postageDto.getPostageType());
        postage.setStatus(postageDto.getStatus());
        postage.setPerson(personMapper.toEntity( postageDto.getPerson()));
        postage.setTrackingList(trackingMapper.toEntityList(postageDto.getTrackingList()));

        if (Objects.nonNull(postage.getTrackingList())) {
            postage.getTrackingList().forEach(
                    tracking -> tracking.setPostage(postage)
            );
        }

        return postage;
    }
}
