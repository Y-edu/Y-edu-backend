alter table yedu.matching_timetable
drop column application_form_application_form_id;

alter table yedu.matching_timetable
    add column class_matching_id bigint;
