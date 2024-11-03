package ru.zvrg.mailtrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zvrg.mailtrace.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
