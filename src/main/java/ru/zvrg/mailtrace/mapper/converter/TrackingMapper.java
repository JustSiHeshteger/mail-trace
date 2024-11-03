package ru.zvrg.mailtrace.mapper.converter;

import org.mapstruct.Mapper;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.dto.TrackingDto;
import ru.zvrg.mailtrace.entity.Tracking;

import java.util.List;

@Mapper(componentModel = "spring",
    uses = {PostOfficeDto.class,
            AddressMapper.class}
)
public interface TrackingMapper {

    TrackingDto toDto(Tracking tracking);
    Tracking toEntity(TrackingDto trackingDto);
    List<TrackingDto> toDtoList(List<Tracking> trackingList);
    List<Tracking> toEntityList(List<TrackingDto> trackingDtoList);
}
