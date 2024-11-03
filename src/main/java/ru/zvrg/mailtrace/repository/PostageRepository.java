package ru.zvrg.mailtrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zvrg.mailtrace.entity.Postage;

import java.util.UUID;

@Repository
public interface PostageRepository extends JpaRepository<Postage, UUID> {
    Postage getPostageByIdentifier(UUID postageId);
}
