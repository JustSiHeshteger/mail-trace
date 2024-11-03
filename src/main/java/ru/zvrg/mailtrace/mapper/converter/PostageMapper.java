package ru.zvrg.mailtrace.mapper.converter;

import org.mapstruct.Mapper;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.dto.PostageDto;
import ru.zvrg.mailtrace.entity.Postage;

@Mapper(componentModel = "spring",
    uses = {PersonMapper.class,
            AddressMapper.class,
            TrackingMapper.class,
            PostOfficeDto.class}
)
public interface PostageMapper {

    PostageDto toDto(Postage postage);
    Postage toEntity(PostageDto postageDto);
}
