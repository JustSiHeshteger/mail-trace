package ru.zvrg.mailtrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zvrg.mailtrace.entity.PostOffice;

import java.util.List;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
    @Query("SELECT postOffice FROM PostOffice postOffice " +
           "JOIN FETCH postOffice.address address " +
           "WHERE LOWER(postOffice.postOfficeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(address.street) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(address.postalCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PostOffice> findPostOfficeByTermContaining(@Param("searchTerm") String searchTerm);
}
