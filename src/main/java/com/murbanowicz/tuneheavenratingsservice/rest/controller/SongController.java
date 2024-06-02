package com.murbanowicz.tuneheavenratingsservice.rest.controller;

import com.murbanowicz.tuneheavenratingsservice.rest.dto.AverageMonthlySongRating;
import com.murbanowicz.tuneheavenratingsservice.rest.dto.AverageSongRatingPeriodResponse;
import com.murbanowicz.tuneheavenratingsservice.service.AverageMonthlyRatingService;
import com.murbanowicz.tuneheavenratingsservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SongController {

    private final RatingService ratingService;
    private final AverageMonthlyRatingService averageMonthlyRatingService;

    @GetMapping("/{songUuid}/avg")
    public AverageSongRatingPeriodResponse getAverageSongRatingForPeriod(
            @PathVariable String songUuid,
            @RequestParam String since,
            @RequestParam String until
    ) {
        return ratingService.getAverageSongRatingForPeriod(songUuid, since, until);
    }

    @GetMapping("/{songUuid}/avg-three-months")
    public List<AverageMonthlySongRating> getAverageSongRatingsForLastThreeMonths(
            @PathVariable String songUuid
    ) {
        return averageMonthlyRatingService.getAverageSongRatingsForLastThreeMonths(songUuid);
    }
}