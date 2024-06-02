package com.murbanowicz.tuneheavenratingsservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ImportFileDataRow {

    private String songName;
    private UUID songUuid;
    private Integer reviewRating;
}