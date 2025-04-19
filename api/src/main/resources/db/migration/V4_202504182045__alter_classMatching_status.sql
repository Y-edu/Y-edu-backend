ALTER TABLE class_matching
    MODIFY COLUMN match_status ENUM('거절', '대기', '수락', '전송', '입금확인', '매칭', '최종매칭', '과외결렬')
    NOT NULL
    DEFAULT '대기';
