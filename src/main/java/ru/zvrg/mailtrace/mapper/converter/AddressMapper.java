package ru.zvrg.mailtrace.mapper.converter;

import org.mapstruct.Mapper;
import ru.zvrg.mailtrace.dto.AddressDto;
import ru.zvrg.mailtrace.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(Address address);
    Address toEntity(AddressDto addressDto);
}
