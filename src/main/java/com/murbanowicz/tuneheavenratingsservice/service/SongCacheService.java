package com.murbanowicz.tuneheavenratingsservice.service;

import com.murbanowicz.tuneheavenratingsservice.persistence.entity.Song;
import com.murbanowicz.tuneheavenratingsservice.persistence.repository.SongRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongCacheService {

    private final SongRepository songRepository;
    private final Map<UUID, Long> songCache = new HashMap<>();
    private static final int PAGE_SIZE = 1000;

    @PostConstruct
    private void initCache() {
        long totalSongsAmount = songRepository.count();
        long totalPagesAmount = totalSongsAmount / PAGE_SIZE;

        log.info(String.format("Initializing song cache, db song records amount: %d", totalSongsAmount));
        for (int pageNumber=0 ; pageNumber<totalPagesAmount; pageNumber++) {
            songRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE)).getContent()
                    .forEach(song -> songCache.put(song.getUuid(), song.getId()));
        }
    }

    public Long getSongId(UUID uuid) {
        return songCache.get(uuid);
    }

    public void addSong(Song song) {
        songCache.put(song.getUuid(), song.getId());
    }

    public boolean containsSongUuid(UUID uuid) {
        return songCache.containsKey(uuid);
    }
}