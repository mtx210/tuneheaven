package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.model.ImportFileDataRow;
import com.murbanowicz.tuneheavenratingsservice.persistence.entity.ProcessedFile;
import com.murbanowicz.tuneheavenratingsservice.persistence.entity.Rating;
import com.murbanowicz.tuneheavenratingsservice.persistence.entity.Song;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.ProcessedFileRepository;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.RatingRepository;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportSongRatingsService {

    private final SongCacheService songCacheService;
    private final SongRepository songRepository;
    private final RatingRepository ratingRepository;
    private final ProcessedFileRepository processedFileRepository;
    private static final int SONG_NAME_CSV_INDEX = 0;
    private static final int SONG_ID_CSV_INDEX = 1;
    private static final int REVIEW_RATING_CSV_INDEX = 5;
    private static final String ARCHIVED_FILE_SUFFIX = "_archived";

    @Transactional
    public void importFromFile(File importFile, LocalDateTime dateTimeNow) {
        Optional<ProcessedFile> processedFileEntity = processedFileRepository.findByFileName(importFile.getName());
        if (processedFileEntity.isPresent()) {
            archiveFile(importFile);
            log.info(String.format("File: %s has already been processed before. File has been archived.", importFile.getName()));
            return;
        }

        List<ImportFileDataRow> importFileDataRows;
        try (Stream<String> lines = Files.lines(Paths.get(importFile.getAbsolutePath()))) {
            importFileDataRows = lines
                    .skip(1L)
                    .map(this::mapCsvLineToImportFileDataRow)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot access import file: %s", importFile.getName()), e);
        }

        List<Song> missingDatabaseSongs = getMissingDatabaseSongs(importFileDataRows);
        if (!missingDatabaseSongs.isEmpty()) {
            songRepository.saveAll(missingDatabaseSongs).forEach(songCacheService::addSong);
        }

        List<Rating> ratings = importFileDataRows.stream()
                .map(importFileDataRow -> Rating.builder()
                        .songId(getSongId(importFileDataRow.getSongUuid()))
                        .ratingValue(importFileDataRow.getReviewRating())
                        .rateDate(dateTimeNow)
                        .build())
                .toList();
        ratingRepository.saveAll(ratings);

        processedFileRepository.save(ProcessedFile.builder()
                .fileName(importFile.getName())
                .dateProcessed(dateTimeNow)
                .build());
    }

    private Long getSongId(UUID songUuid) {
        return Optional.of(songCacheService.getSongId(songUuid))
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find song with uuid: %s", songUuid.toString())));
    }

    private void archiveFile(File importFile) {
        try {
            Files.move(importFile.toPath(), importFile.toPath().resolveSibling(importFile.getName().concat(ARCHIVED_FILE_SUFFIX)));
        } catch (IOException e) {
            log.warn(String.format("Failed to archive file: %s", importFile.getName()));
        }
    }

    private ImportFileDataRow mapCsvLineToImportFileDataRow(String csvLine) {
        List<String> csvElements = Arrays.asList(csvLine.split(","));
        return ImportFileDataRow.builder()
                .songName(csvElements.get(SONG_NAME_CSV_INDEX))
                .songUuid(UUID.fromString(csvElements.get(SONG_ID_CSV_INDEX)))
                .reviewRating(Integer.valueOf(csvElements.get(REVIEW_RATING_CSV_INDEX)))
                .build();
    }

    private List<Song> getMissingDatabaseSongs(List<ImportFileDataRow> importFileDataRows) {
        List<Song> missingSongs = new ArrayList<>();

        importFileDataRows.forEach(importFileDataRow -> {
            UUID songUuid = importFileDataRow.getSongUuid();
            if (!songCacheService.containsSongUuid(songUuid)) {
                missingSongs.add(Song.builder()
                        .uuid(songUuid)
                        .name(importFileDataRow.getSongName())
                        .build()
                );
            }
        });

        return missingSongs;
    }
}