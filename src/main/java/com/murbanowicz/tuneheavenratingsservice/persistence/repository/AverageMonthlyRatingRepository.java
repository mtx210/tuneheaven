package com.murbanowicz.tuneheavenratingsservice.persistence.repository;

import com.murbanowicz.tuneheavenratingsservice.persistence.entity.AverageMonthlyRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AverageMonthlyRatingRepository extends JpaRepository<AverageMonthlyRating, Long> {

    @Query(value = """
           SELECT a.average_rating_value, a.average_rating_month
           FROM average_monthly_rating a
           WHERE a.song_id = :songId
           ORDER BY a.average_rating_month DESC
           LIMIT 3
           """, nativeQuery = true)
    List<Object[]> findAverageSongRatingForLastThreeMonths(Long songId);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO average_monthly_rating (song_id, average_rating_value, average_rating_month) 
            SELECT song_id, AVG(rating_value) AS avg_rating, :averageRatingMonth 
            FROM rating 
            WHERE rate_date BETWEEN :dateSince AND :dateUntil 
            GROUP BY song_id""", nativeQuery = true)
    void saveAverageMonthlySongRatings(LocalDate dateSince, LocalDate dateUntil, String averageRatingMonth);

    @Query(value = """
           SELECT
                s.name,
                s.uuid,
                a.average_rating_month,
                b.average_rating_month,
                c.average_rating_month,
                (a.average_rating_value - b.average_rating_value) AS difference
           FROM song s
           LEFT JOIN average_monthly_rating a ON a.song_id = s.id
           LEFT JOIN average_monthly_rating b ON b.song_id = s.id AND b.average_rating_month = :previousMonth
           LEFT JOIN average_monthly_rating c ON c.song_id = s.id AND c.average_rating_month = :twoMonthsBack
           WHERE a.average_rating_month = :currentMonth
           ORDER BY difference DESC
           LIMIT 100""", nativeQuery = true)
    List<Object[]> findTop100SongsByAverageRatingIncrease(String currentMonth, String previousMonth, String twoMonthsBack);

    @Query(value = """
           SELECT
                s.name,
                s.uuid,
                a.average_rating_month,
                b.average_rating_month,
                c.average_rating_month,
                (b.average_rating_value - a.average_rating_value >= 0.4) AS difference
           FROM song s
           LEFT JOIN average_monthly_rating a ON a.song_id = s.id
           LEFT JOIN average_monthly_rating b ON b.song_id = s.id AND b.average_rating_month = :previousMonth
           LEFT JOIN average_monthly_rating c ON c.song_id = s.id AND c.average_rating_month = :twoMonthsBack
           WHERE a.average_rating_month = :currentMonth
           ORDER BY difference DESC""", nativeQuery = true)
    List<Object[]> findAllSongsWithAverageRatingDecreasePointFour(String currentMonth, String previousMonth, String twoMonthsBack);
}