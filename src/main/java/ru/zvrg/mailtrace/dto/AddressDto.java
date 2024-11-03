package ru.zvrg.mailtrace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.zvrg.mailtrace.entity.Address;

/**
 * DTO for {@link Address}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    Long id;
    @Size(max = 50)
    @NotBlank
    String country;
    @Size(max = 50)
    @NotBlank
    String town;
    @Size(max = 100)
    @NotBlank
    String street;
    @Size(max = 20)
    @NotBlank
    String postalCode;
}