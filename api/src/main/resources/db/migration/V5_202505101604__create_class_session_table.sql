DROP TABLE IF EXISTS class_session;

CREATE TABLE class_session
(
    class_session_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    class_management_id BIGINT COMMENT 'Class Management ID',
    session_date DATE NOT NULL COMMENT '실제 수업 날짜',
    understanding_and_attitude TEXT COMMENT '아이의 이해도 & 학습태도',
    cancel BOOLEAN DEFAULT FALSE COMMENT '휴강 여부',
    cancel_reason VARCHAR(255) COMMENT '휴강 사유',
    completed BOOLEAN DEFAULT FALSE COMMENT '수업 완료 여부',
    start TIME COMMENT '수업 시작 시간',
    class_minute INT COMMENT '수업 시간',
    created_at DATETIME(6) NULL COMMENT '생성일',
    updated_at DATETIME(6) NULL COMMENT '수정일'
);
