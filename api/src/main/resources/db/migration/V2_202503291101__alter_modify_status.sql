ALTER TABLE teacher
    MODIFY COLUMN status ENUM('등록중', '활동중', '일시정지', '종료', '등록폼작성완료', '사진영상제출완료');
UPDATE teacher
SET status = '등록폼작성완료'
WHERE status = '등록중';
ALTER TABLE teacher
    MODIFY COLUMN status ENUM('활동중', '일시정지', '종료', '등록폼작성완료', '사진영상제출완료');
