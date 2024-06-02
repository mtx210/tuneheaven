package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.exception.RestArgumentsException;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.RatingRepository;
import com.murbanowicz.tuneheavenratingsservice.rest.dto.AverageSongRatingPeriodResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final SongCacheService songCacheService;
    private final RatingRepository ratingRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

    public AverageSongRatingPeriodResponse getAverageSongRatingForPeriod(
            String songUuid,
            String since,
            String until
    ) {
        try {
            UUID songUuidParsed = UUID.fromString(songUuid);
            LocalDateTime dateSince = LocalDate.parse(since, dtf).atStartOfDay();
            LocalDateTime dateUntil = LocalDate.parse(until, dtf).atTime(23, 59, 59);

            Long songId = songCacheService.getSongId(songUuidParsed);
            Double averageSongRatingForPeriod = ratingRepository.getAverageSongRatingForPeriod(songId, dateSince, dateUntil);
            return new AverageSongRatingPeriodResponse(averageSongRatingForPeriod);
        } catch (IllegalArgumentException e) {
            throw new RestArgumentsException("Invalid song uuid format");
        } catch (DateTimeParseException e) {
            throw new RestArgumentsException("Invalid date argument format");
        }
    }
}