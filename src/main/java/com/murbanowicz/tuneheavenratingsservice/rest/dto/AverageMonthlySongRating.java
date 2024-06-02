package com.murbanowicz.tuneheavenratingsservice.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class AverageMonthlySongRating {

    private final String month;
    private final Double avg;
}