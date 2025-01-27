INSERT INTO processed_file (id, file_name, date_processed) VALUES
(1, 'tuneheaven-songs-2024-03-02.csv', '2024-03-02 12:00:00.000000'),
(2, 'tuneheaven-songs-2024-04-02.csv', '2024-04-02 12:00:00.000000'),
(3, 'tuneheaven-songs-2024-05-02.csv', '2024-05-02 12:00:00.000000');

INSERT INTO song (id, uuid, name) VALUES
(1, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 'SongName1'),
(2, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 'SongName2'),
(3, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')) ,'SongName3');

INSERT INTO rating (id, song_id, rating_value, rate_date) VALUES
(1,1,2,'2024-03-02 12:00:00.000000'),
(2,2,2,'2024-03-02 12:00:00.000000'),
(3,3,2,'2024-03-02 12:00:00.000000'),

(4,1,2,'2024-03-03 12:00:00.000000'),
(5,2,3,'2024-03-03 12:00:00.000000'),
(6,3,4,'2024-03-03 12:00:00.000000'),

(7,1,2,'2024-04-02 12:00:00.000000'),
(8,2,3,'2024-04-02 12:00:00.000000'),
(9,3,5,'2024-04-02 12:00:00.000000'),

(10,1,1,'2024-04-03 12:00:00.000000'),
(11,2,3,'2024-04-03 12:00:00.000000'),
(12,3,5,'2024-04-03 12:00:00.000000'),

(13,1,1,'2024-05-02 12:00:00.000000'),
(14,2,5,'2024-05-02 12:00:00.000000'),
(15,3,5,'2024-05-02 12:00:00.000000'),

(16,1,1,'2024-05-03 12:00:00.000000'),
(17,2,5,'2024-05-03 12:00:00.000000'),
(18,3,4,'2024-05-03 12:00:00.000000');

INSERT INTO tunes.average_monthly_rating (id, song_id, average_rating_value, average_rating_month) VALUES
(1, 1, 2, '202403'),
(2, 2, 2.5, '202403'),
(3, 3, 3, '202403'),

(4, 1, 1.5, '202404'),
(5, 2, 3, '202404'),
(6, 3, 5, '202404'),

(7, 1, 1, '202405'),
(8, 2, 5, '202405'),
(9, 3, 4.5, '202405');