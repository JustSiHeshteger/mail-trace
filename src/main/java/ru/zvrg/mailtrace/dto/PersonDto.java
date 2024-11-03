package ru.zvrg.mailtrace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.zvrg.mailtrace.entity.Person;

/**
 * DTO for {@link Person}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    Long id;
    @NotNull
    @Size(max = 50)
    @NotBlank
    String firstName;
    @Size(max = 50)
    @NotBlank
    String secondName;
    @Size(max = 50)
    String thirdName;
    @Size(max = 12)
    @NotBlank
    String phoneNumber;
    @Valid
    AddressDto address;
}