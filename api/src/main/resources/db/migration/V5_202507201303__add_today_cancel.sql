ALTER TABLE class_session
    ADD COLUMN is_today_cancel BOOLEAN DEFAULT FALSE COMMENT '당일 취소 여부';
