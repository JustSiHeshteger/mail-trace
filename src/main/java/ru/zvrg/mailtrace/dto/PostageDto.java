package ru.zvrg.mailtrace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.zvrg.mailtrace.common.PostageStatus;
import ru.zvrg.mailtrace.common.PostageType;
import ru.zvrg.mailtrace.entity.Postage;

import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Postage}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostageDto {
    UUID identifier;
    @NotNull
    PostageType postageType;
    @NotNull
    PostageStatus status;
    @Valid
    PersonDto person;
    List<TrackingDto> trackingList;
}