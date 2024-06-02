package com.murbanowicz.tuneheavenratingsservice.persistence.repository;

import com.murbanowicz.tuneheavenratingsservice.persistence.entity.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, Long> {

    Optional<ProcessedFile> findByFileName(String fileName);
}