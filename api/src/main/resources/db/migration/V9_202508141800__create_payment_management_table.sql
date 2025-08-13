CREATE TABLE IF NOT EXISTS yedu.payment_management (
     bill_id VARCHAR(100) PRIMARY KEY NOT NULL COMMENT '청구서 ID',
     application_form_id varchar(255) NOT NULL COMMENT '신청서 ID',
     session_ids VARCHAR(20) NOT NULL COMMENT '세션 ID',
     phone_number varchar(20) NOT NULL COMMENT '전화번호',
     total_minutes int NOT NULL COMMENT '총 분',
     message VARCHAR(2000) NOT NULL COMMENT '메시지',
     price int NOT NULL COMMENT '가격',
     isPaid BOOLEAN NOT NULL DEFAULT FALSE COMMENT '결제 여부',
     pay_response JSON DEFAULT NULL COMMENT '결제 응답 상세 내역',
     expired_at datetime(6) DEFAULT NULL COMMENT '만료 일시',
     created_at datetime(6) DEFAULT NULL COMMENT '생성 일시',
     appr_dt datetime(6) DEFAULT NULL COMMENT '승인 일시'
);
