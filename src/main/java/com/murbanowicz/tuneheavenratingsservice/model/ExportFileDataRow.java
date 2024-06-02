package com.murbanowicz.tuneheavenratingsservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ExportFileDataRow {

    private String songName;
    private UUID songUUid;
    private Double ratingThisMonth;
    private Double ratingPreviousMonth;
    private Double ratingTwoMonthsBack;

    public String toCsvFormat() {
        return String.format("%s,%s,%f,%f,%f",
                songName,
                songUUid.toString(),
                ratingThisMonth,
                ratingPreviousMonth,
                ratingTwoMonthsBack
        );
    }
}