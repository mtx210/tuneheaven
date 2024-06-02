package com.murbanowicz.tuneheavenratingsservice.rest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AverageSongRatingPeriodResponse {

    private final Double averageRating;
}