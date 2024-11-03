package ru.zvrg.mailtrace.mapper.converter;

import org.mapstruct.Mapper;
import ru.zvrg.mailtrace.dto.PersonDto;
import ru.zvrg.mailtrace.entity.Person;

@Mapper(componentModel = "spring",
    uses = {AddressMapper.class}
)
public interface PersonMapper {

    PersonDto toDto(Person person);
    Person toEntity(PersonDto personDto);
}
