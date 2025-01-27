create table if not exists yedu.teacher
(
    alert_message_count  int                                                                                 not null,
    class_count          int                                                                                 not null,
    english_possible     bit                                                                                 not null,
    gender               bit                                                                                 not null,
    marketing_agree      bit                                                                                 not null,
    math_possible        bit                                                                                 not null,
    response_rate        double                                                                              not null,
    response_time        double                                                                              not null,
    created_at           datetime                                                                         null,
    teacher_id           bigint auto_increment
    primary key,
    updated_at           datetime                                                                         null,
    birth                varchar(30)                                                                        not null,
    comment              text                                                                                not null,
    email                varchar(60)                                                                        not null,
    high_school          varchar(90)                                                                        not null,
    high_school_type     varchar(60)                                                                        not null,
    introduce            text                                                                                not null,
    issue                text                                                                                null,
    major                varchar(90)                                                                        not null,
    name                 varchar(30)                                                                        not null,
    nick_name            varchar(30)                                                                        not null,
    phone_number         varchar(30)                                                                        not null,
    profile              varchar(255)                                                                        null,
    recommend_student    text                                                                                not null,
    source               varchar(60)                                                                        not null,
    teaching_style_info1 text                                                                                not null,
    teaching_style_info2 text                                                                                not null,
    terminate_issue      text                                                                                null,
    university           varchar(30)                                                                        not null,
    video                varchar(255)                                                                        null,
    grade                enum ('PRO', 'STANDARD')                                                            null,
    status               enum ('ACTIVE', 'PAUSED', 'TERMINATED', 'PENDING')                                             null,
    teaching_style1      enum ('CARING', 'CONFIDENCE_BOOSTER', 'FOCUSED', 'FUN', 'METICULOUS', 'PASSIONATE', 'ERROR') not null,
    teaching_style2      enum ('CARING', 'CONFIDENCE_BOOSTER', 'FOCUSED', 'FUN', 'METICULOUS', 'PASSIONATE', 'ERROR') not null
    );

create table if not exists yedu.teacher_available
(
    available_time     time                                  not null,
    available_id       bigint auto_increment
    primary key,
    teacher_teacher_id bigint                                   null,
    day                enum ('금', '목', '수', '월', '일', '토', '화', 'ERROR') not null,
    constraint FKbudvjpq2cb1i6ahsr0g3pk6lo
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_district
(
    district_id        bigint auto_increment
    primary key,
    teacher_teacher_id bigint                                                                                                                                                                                                                              null,
    district           enum ('강남구', '강동구', '강북구', '강서구', '경기지역', '고양', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '분당', '서대문구', '서초구', '성동구', '성북구', '송파구', '안양', '양천구', '영등포구', '온라인', '용산구', '용인', '은평구', '인천', '종로구', '중구', '중랑구', 'ERROR') not null,
    constraint FKf1hjhkynppv6kdob6tfwwkdko
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_english
(
    english_id          bigint auto_increment
    primary key,
    teacher_teacher_id  bigint null,
    appeal_point        text   not null,
    foreign_experience  text   null,
    management_style    text   not null,
    teaching_experience text   not null,
    teaching_history    int   not null,
    teaching_style      text   not null,
    constraint FKt7o0686li2wkthuf8qibtr11r
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_math
(
    math_id             bigint auto_increment
    primary key,
    teacher_teacher_id  bigint null,
    appeal_point        text   not null,
    management_style    text   not null,
    teaching_experience text   not null,
    teaching_history    int   not null,
    teaching_style      text   not null,
    constraint FK84pjet9p2vlfubiqi89dg6uah
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );