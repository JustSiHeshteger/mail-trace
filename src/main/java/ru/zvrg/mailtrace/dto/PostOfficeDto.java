package ru.zvrg.mailtrace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.zvrg.mailtrace.entity.PostOffice;

/**
 * DTO for {@link PostOffice}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOfficeDto {
    Long id;
    @Size(max = 20)
    @NotBlank
    String postOfficeName;
    @Valid
    AddressDto address;
}