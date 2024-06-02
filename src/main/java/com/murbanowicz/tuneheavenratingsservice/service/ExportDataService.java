package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.model.ExportFileDataRow;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.AverageMonthlyRatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportDataService {

    private final AverageMonthlyRatingRepository averageMonthlyRatingRepository;
    private static final DateTimeFormatter YEAR_MONTH_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final String EXPORT_CSV_FILE_HEADER = "song_name,song_uuid,rating_this_month,rating_previous_month,rating_2months_back";
    @Value("${csv.export.trending-songs-file-dir}")
    private String exportTrendingSongsFileDir;
    @Value("${csv.export.trending-songs-file-mask}")
    private String exportTrendingSongsFileMask;
    @Value("${csv.export.loosing-songs-file-dir}")
    private String exportLoosingSongsFileDir;
    @Value("${csv.export.loosing-songs-file-mask}")
    private String exportLoosingSongsFileMask;

    // TODO extract common
    public void exportTrendingSongsFile(LocalDateTime dateTimeNow) {
        LocalDate dateNow = dateTimeNow.toLocalDate();

        List<ExportFileDataRow> exportFileDataRows = averageMonthlyRatingRepository.findTop100SongsByAverageRatingIncrease(
                dateNow.format(YEAR_MONTH_PATTERN),
                dateNow.minusMonths(1L).format(YEAR_MONTH_PATTERN),
                dateNow.minusMonths(2L).format(YEAR_MONTH_PATTERN)
        ).stream()
                .map(this::mapDatabaseResponse)
                .toList();

        File csvOutputFile = new File(exportTrendingSongsFileDir + String.format(exportTrendingSongsFileMask, dateNow.format(YEAR_MONTH_PATTERN)));
        try (PrintWriter printWriter = new PrintWriter(csvOutputFile)) {
            printWriter.println(EXPORT_CSV_FILE_HEADER);
            exportFileDataRows.stream()
                    .map(exportFileDataRow -> escapeSpecialCharacters(exportFileDataRow.toCsvFormat()))
                    .forEach(printWriter::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Error while exporting file: %s", csvOutputFile.getName()), e);
        }
    }

    public void exportLoosingSongsFile(LocalDateTime dateTimeNow) {
        LocalDate dateNow = dateTimeNow.toLocalDate();

        List<ExportFileDataRow> exportFileDataRows = averageMonthlyRatingRepository.findAllSongsWithAverageRatingDecreasePointFour(
                        dateNow.format(YEAR_MONTH_PATTERN),
                        dateNow.minusMonths(1L).format(YEAR_MONTH_PATTERN),
                        dateNow.minusMonths(2L).format(YEAR_MONTH_PATTERN)
                ).stream()
                .map(this::mapDatabaseResponse)
                .toList();

        File csvOutputFile = new File(exportLoosingSongsFileDir + String.format(exportLoosingSongsFileMask, dateNow.format(YEAR_MONTH_PATTERN)));
        try (PrintWriter printWriter = new PrintWriter(csvOutputFile)) {
            printWriter.println(EXPORT_CSV_FILE_HEADER);
            exportFileDataRows.stream()
                    .map(exportFileDataRow -> escapeSpecialCharacters(exportFileDataRow.toCsvFormat()))
                    .forEach(printWriter::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Error while exporting file: %s", csvOutputFile.getName()), e);
        }
    }

    private ExportFileDataRow mapDatabaseResponse(Object[] dbResponse) {
        return ExportFileDataRow.builder()
                .songName(String.valueOf(dbResponse[0]))
                .songUUid(UUID.fromString(String.valueOf(dbResponse[1])))
                .ratingThisMonth((double) dbResponse[2])
                .ratingPreviousMonth((double) dbResponse[3])
                .ratingTwoMonthsBack((double) dbResponse[4])
                .build();
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}