package ru.zvrg.mailtrace.mapper.converter;

import org.mapstruct.Mapper;
import ru.zvrg.mailtrace.dto.PostOfficeDto;
import ru.zvrg.mailtrace.entity.PostOffice;

import java.util.List;

@Mapper(componentModel = "spring",
    uses = {AddressMapper.class}
)
public interface PostOfficeMapper {

   PostOfficeDto toDto(PostOffice postOffice);
   PostOffice toEntity(PostOfficeDto postOfficeDto);
   List<PostOfficeDto> toDtoList(List<PostOffice> postOfficeList);
   List<PostOffice> toEntityList(List<PostOfficeDto> postOfficeDtoList);
}
