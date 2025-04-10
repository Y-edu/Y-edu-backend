ALTER TABLE goal MODIFY COLUMN class_goal text not null;
ALTER TABLE application_form MODIFY COLUMN want_time text not null;
ALTER TABLE application_form MODIFY COLUMN source text not null;
ALTER TABLE teacher MODIFY COLUMN recommend_student text;
ALTER TABLE teacher MODIFY COLUMN high_school_type varchar(255);
ALTER TABLE teacher_english MODIFY COLUMN appeal_point text;
ALTER TABLE teacher_english MODIFY COLUMN management_style text;
ALTER TABLE teacher_math MODIFY COLUMN appeal_point text;
ALTER TABLE teacher_math MODIFY COLUMN management_style text;
