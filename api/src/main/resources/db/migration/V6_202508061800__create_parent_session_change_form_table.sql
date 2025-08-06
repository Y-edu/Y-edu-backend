CREATE TABLE IF NOT EXISTS yedu.session_change_form (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     parents_id BIGINT,
     session_id BIGINT,
     change_type VARCHAR(50),
     created_at  datetime(6)                                                                                                                                                                                                                                  null,
     updated_at  datetime(6)
);
