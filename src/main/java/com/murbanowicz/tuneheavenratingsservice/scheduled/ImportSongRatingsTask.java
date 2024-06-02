package com.murbanowicz.tuneheavenratingsservice.scheduled;

import com.murbanowicz.tuneheavenratingsservice.service.AverageMonthlyRatingService;
import com.murbanowicz.tuneheavenratingsservice.service.ExportDataService;
import com.murbanowicz.tuneheavenratingsservice.service.ImportSongRatingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportSongRatingsTask {

    private final ImportSongRatingsService importSongRatingsService;
    private final ExportDataService exportDataService;
    private final AverageMonthlyRatingService averageMonthlyRatingService;
    @Value("${csv.import.ratings-file-dir}")
    private String importRatingsFileDir;
    @Value("${csv.import.ratings-file-mask}")
    private String importRatingsFileMask;

    @Scheduled(cron = "${scheduled-tasks.import-song-ratings-cron}")
    @EventListener(ApplicationReadyEvent.class)
    public void importSongRatings() {
        LocalDateTime dateTimeNow = LocalDateTime.now();

        List<File> importFiles = getFilesMatchingMask();
        for (File importFile : importFiles) {
            importSongRatingsService.importFromFile(importFile, dateTimeNow);
        }

        if (lastDayOfMonth()) {
            averageMonthlyRatingService.calculateAndSaveAverageMonthlySongRatings(dateTimeNow);

            exportDataService.exportTrendingSongsFile(dateTimeNow);
            exportDataService.exportLoosingSongsFile(dateTimeNow);
        }
    }

    private List<File> getFilesMatchingMask() {
        log.info(String.format("Import folder set as: %s", importRatingsFileDir));

        File importFolder = new File(importRatingsFileDir);
        if (!importFolder.exists() || !importFolder.isDirectory()) {
            throw new RuntimeException(String.format("Provided input file folder path does not exist: %s", importRatingsFileDir));
        }

        File[] importFiles = importFolder.listFiles((dir, name) -> name.matches(importRatingsFileMask));
        if (importFiles == null || importFiles.length < 1) {
            throw new RuntimeException("No import files matching file mask found");
        }

        log.info(String.format("Found %d files matching import file mask", importFiles.length));
        return Arrays.asList(importFiles);
    }

    private boolean lastDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return today.equals(lastDayOfMonth);
    }
}