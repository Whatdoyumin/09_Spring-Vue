DROP TABLE IF EXISTS tbl_travel_image;
DROP TABLE IF EXISTS tbl_travel;

# 여행지 테이블
CREATE TABLE tbl_travel
(
    no          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    district    VARCHAR(50)        NOT NULL,
    title       VARCHAR(512)       NOT NULL,
    description TEXT,
    address     VARCHAR(512),
    phone       VARCHAR(256)
);


# 여행지 이미지 테이블
CREATE TABLE tbl_travel_image
(
    no        INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    filename  VARCHAR(512)       NOT NULL,
    travel_no INT,
    CONSTRAINT FOREIGN KEY (travel_no) REFERENCES tbl_travel (no)
        ON DELETE CASCADE
);

-- 001-1.jpg부터 112-5.jpg까지 총 560개 파일명 INSERT
-- MySQL의 재귀 CTE(Common Table Expression) 사용 (MySQL 8.0 이상만 가능)
INSERT INTO tbl_travel_image (filename, travel_no)
WITH RECURSIVE file_generator AS (
    -- 초기값: 첫 번째 파일 (001-1.jpg)
    SELECT 1 as x, 1 as y

    UNION ALL

    -- 재귀 부분: 다음 파일명 생성
    SELECT CASE
               WHEN y < 5 THEN x -- y가 5보다 작으면 x 유지
               ELSE x + 1 -- y가 5이면 x 증가
               END as x,
           CASE
               WHEN y < 5 THEN y + 1 -- y가 5보다 작으면 y 증가
               ELSE 1 -- y가 5이면 1로 리셋
               END as y
    FROM file_generator
    WHERE x <= 112
      AND NOT (x = 112 AND y = 5) -- 112-5까지만 생성
)
SELECT CONCAT(LPAD(x, 3, '0'), '-', y, '.jpg') as filename,
       x                                       as travel_no -- travel_no는 나중에 UPDATE로 설정
FROM file_generator
WHERE x <= 112;
