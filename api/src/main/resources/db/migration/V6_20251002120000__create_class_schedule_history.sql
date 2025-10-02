
DROP TABLE IF EXISTS class_schedule_history;

CREATE TABLE class_schedule_history
(
    class_schedule_history_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    class_management_id BIGINT COMMENT 'Class Management ID',
    day ENUM ('금', '목', '수', '월', '일', '토', '화') NOT NULL COMMENT '수업이 진행되는 요일',
    start TIME COMMENT '수업 시작 시간',
    class_minute INT COMMENT '수업 시간',
    applied_at DATE NULL COMMENT '과외 변경 시작일',
    created_at DATETIME(6) NULL COMMENT '생성일',
    updated_at DATETIME(6) NULL COMMENT '수정일'
);
