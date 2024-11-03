package ru.zvrg.mailtrace.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zvrg.mailtrace.common.exception.MailTraceException;
import ru.zvrg.mailtrace.entity.Address;
import ru.zvrg.mailtrace.repository.AddressRepository;

import static ru.zvrg.mailtrace.common.consts.ExceptionConst.ADDRESS_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address getAddressById(@NotNull Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new MailTraceException(ADDRESS_ID_NOT_FOUND));
    }
}
