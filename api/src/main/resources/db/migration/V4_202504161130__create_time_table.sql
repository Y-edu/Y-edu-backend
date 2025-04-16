create table if not exists yedu.application_form_available
(
    available_id       bigint auto_increment primary key,
    available_time     time(6)                                  not null,
    day                enum ('금', '목', '수', '월', '일', '토', '화') not null,
    application_form_application_form_id bigint  null,
    created_at  datetime(6)                     null,
    updated_at  datetime(6)                     null
);

create table if not exists yedu.matching_timetable
(
    timetable_id       bigint auto_increment primary key,
    timetable_time     time(6)                                  not null,
    day                enum ('금', '목', '수', '월', '일', '토', '화') not null,
    application_form_application_form_id bigint  null,
    created_at  datetime(6)                     null,
    updated_at  datetime(6)                     null
);
