package com.murbanowicz.tuneheavenratingsservice.persistence.repository;

import com.murbanowicz.tuneheavenratingsservice.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("""
        SELECT AVG(R.ratingValue)
        FROM Rating R
        WHERE R.songId = :songId
        AND R.rateDate BETWEEN :dateSince AND :dateUntil
    """)
    Double getAverageSongRatingForPeriod(Long songId, LocalDateTime dateSince, LocalDateTime dateUntil);
}