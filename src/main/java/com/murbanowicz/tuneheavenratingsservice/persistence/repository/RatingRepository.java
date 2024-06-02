package com.murbanowicz.tuneheavenratingsservice.persistence.repository;

import com.murbanowicz.tuneheavenratingsservice.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {


}