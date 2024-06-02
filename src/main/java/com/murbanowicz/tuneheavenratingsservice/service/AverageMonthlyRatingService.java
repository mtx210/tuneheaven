package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.persistence.repository.AverageMonthlyRatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AverageMonthlyRatingService {

    private final AverageMonthlyRatingRepository averageMonthlyRatingRepository;
    private static final DateTimeFormatter YEAR_MONTH_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM");

    public void calculateAndSaveAverageMonthlySongRatings(LocalDateTime dateTimeNow) {
        LocalDate dateNow = dateTimeNow.toLocalDate();

        averageMonthlyRatingRepository.saveAverageMonthlySongRatings(
                dateNow.withDayOfMonth(1),
                dateNow,
                dateNow.format(YEAR_MONTH_PATTERN)
        );
    }
}