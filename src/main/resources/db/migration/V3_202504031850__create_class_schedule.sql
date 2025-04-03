DROP TABLE IF EXISTS class_management;

CREATE TABLE class_management
(
    class_management_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    class_matching_id   BIGINT COMMENT 'Class Matching PK',
    textbook            VARCHAR(255) COMMENT '수업에서 사용되는 교재명',
    first_day           DATE COMMENT '첫 수업 날짜',
    start          TIME COMMENT '첫 수업 시작 시간',
    class_minute        INT COMMENT '첫 수업 시간',
    created_at DATETIME(6) NULL COMMENT '생성일',
    updated_at DATETIME(6) NULL COMMENT '수정일'
);


DROP TABLE IF EXISTS class_schedule;

CREATE TABLE class_schedule
(
    class_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    class_management_id BIGINT COMMENT 'Class Management ID',
    day ENUM ('금', '목', '수', '월', '일', '토', '화') NOT NULL COMMENT '수업이 진행되는 요일',
    start TIME COMMENT '수업 시작 시간',
    class_minute INT COMMENT '수업 시간',
    created_at DATETIME(6) NULL COMMENT '생성일',
    updated_at DATETIME(6) NULL COMMENT '수정일'
);
