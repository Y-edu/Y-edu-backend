ALTER TABLE teacher
DROP COLUMN response_rate;

ALTER TABLE teacher
DROP COLUMN response_time;

ALTER TABLE teacher
ADD COLUMN response_count INT COMMENT '응답 횟수';

ALTER TABLE teacher
ADD COLUMN total_request_count INT COMMENT '전체 요청 횟수';
