package ru.zvrg.mailtrace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.zvrg.mailtrace.entity.Tracking;

import java.time.LocalDateTime;

/**
 * DTO for {@link Tracking}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingDto {
    Long id;
    @NotNull
    LocalDateTime timestamp;
    @Valid
    PostOfficeDto postOffice;
}