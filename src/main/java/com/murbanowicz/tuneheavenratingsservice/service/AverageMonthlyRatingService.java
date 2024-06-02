package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.exception.RestArgumentsException;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.AverageMonthlyRatingRepository;
import com.murbanowicz.tuneheavenratingsservice.rest.dto.AverageMonthlySongRating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AverageMonthlyRatingService {

    private final SongCacheService songCacheService;
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

    public List<AverageMonthlySongRating> getAverageSongRatingsForLastThreeMonths(String songUuid) {
        try {
            UUID songUuidParsed = UUID.fromString(songUuid);

            Long songId = songCacheService.getSongId(songUuidParsed);
            return averageMonthlyRatingRepository.findAverageSongRatingForLastThreeMonths(songId).stream()
                    .map(this::mapDatabaseResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new RestArgumentsException("Invalid song uuid format");
        } catch (DateTimeParseException e) {
            throw new RestArgumentsException("Invalid date argument format");
        }
    }

    private AverageMonthlySongRating mapDatabaseResponse(Object[] dbResponse) {
        return AverageMonthlySongRating.builder()
                .avg((double) dbResponse[0])
                .month(String.valueOf(dbResponse[0]))
                .build();
    }
}