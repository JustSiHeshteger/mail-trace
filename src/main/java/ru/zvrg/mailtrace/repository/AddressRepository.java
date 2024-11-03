package ru.zvrg.mailtrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zvrg.mailtrace.entity.Address;

public interface AddressRepository  extends JpaRepository<Address, Long> {
}
