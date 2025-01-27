package com.murbanowicz.tuneheavenratingsservice.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long songId;
    private Integer ratingValue;
    private LocalDateTime rateDate;
}