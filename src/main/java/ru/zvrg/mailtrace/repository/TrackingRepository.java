package ru.zvrg.mailtrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zvrg.mailtrace.entity.Tracking;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    List<Tracking> getTrackingsByPostageIdentifier(UUID postageIdentifier);
}
