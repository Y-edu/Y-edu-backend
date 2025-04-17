alter table yedu.application_form_available
    modify column application_form_application_form_id varchar(255);

alter table yedu.matching_timetable
    modify column application_form_application_form_id varchar(255);

alter table yedu.application_form
    modify column favorite_style text null;
