create table if not exists yedu.parents
(
    parents_id      bigint auto_increment
    primary key,
    created_at      datetime(6)                                                                                                                                                                                                                                  null,
    updated_at      datetime(6)                                                                                                                                                                                                                                  null,
    count           int                                                                                                                                                                                                                                          not null,
    kakao_name      varchar(255)                                                                                                                                                                                                                                 null,
    marketing_agree bit                                                                                                                                                                                                                                          not null,
    phone_number    varchar(255)                                                                                                                                                                                                                                 not null,
    total_payment   int                                                                                                                                                                                                                                          not null
    );

create table if not exists yedu.application_form
(
    application_form_id  varchar(255)            not null
    primary key,
    created_at           datetime(6)             null,
    updated_at           datetime(6)             null,
    age                  varchar(255)            not null,
    class_count          varchar(255)            not null,
    class_time           varchar(255)            not null,
    education_importance enum ('상', '중', '하')    null,
    favorite_condition   text                    null,
    favorite_direction   text                    null,
    favorite_gender      enum ('남', '여', '상관없음') not null,
    favorite_style       text                    not null,
    level                enum ('상', '중', '하')    null,
    online               enum ('대면', '비대면')      not null,
    district        enum ('ERROR', '강남구', '강동구', '강북구', '강서구', '경기지역', '고양', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '분당', '서대문구', '서초구', '성동구', '성북구', '송파구', '안양', '양천구', '영등포구', '온라인', '용산구', '용인', '은평구', '인천', '종로구', '중구', '중랑구') not null,
    dong            varchar(255)                                                                                                                                                                                                                                 not null,
    proceed_status       bit                     not null,
    source               varchar(255)            null,
    want_time            varchar(90)             not null,
    wanted_subject       enum ('수학', '영어')       not null,
    parents_parents_id   bigint                  null,
    constraint FKaxjocxw0tipdk2dds4oyj10vx
    foreign key (parents_parents_id) references yedu.parents (parents_id)
    );

create table if not exists yedu.goal
(
    goal_id                              bigint auto_increment
    primary key,
    class_goal                           varchar(45)  not null,
    application_form_application_form_id varchar(255) null,
    constraint FKshrwp4npsi2g33eqj5wo40egx
    foreign key (application_form_application_form_id) references yedu.application_form (application_form_id)
    );

create table if not exists yedu.settlement
(
    settlement_id      bigint auto_increment
    primary key,
    account_name       varchar(255) null,
    account_number     varchar(255) null,
    bank               varchar(255) null,
    receipt            varchar(255) null,
    parents_parents_id bigint       null,
    constraint FKjh82wa02ej7q85d3hujfippns
    foreign key (parents_parents_id) references yedu.parents (parents_id)
    );

create table if not exists yedu.teacher
(
    teacher_id           bigint auto_increment
    primary key,
    created_at           datetime(6)                                                                                  null,
    updated_at           datetime(6)                                                                                  null,
    alert_message_count  int                                                                                          not null,
    class_count          int                                                                                          not null,
    grade                enum ('PRO', 'STANDARD')                                                                     null,
    issue                text                                                                                         null,
    marketing_agree      bit                                                                                          not null,
    response_rate        double                                                                                       not null,
    response_time        double                                                                                       not null,
    source               varchar(255)                                                                                 not null,
    status               enum ('등록중', '활동중', '일시정지', '종료')                                           null,
    comment              text                                                                                         not null,
    english_possible     bit                                                                                          not null,
    introduce            text                                                                                         not null,
    math_possible        bit                                                                                          not null,
    recommend_student    text                                                                                         not null,
    teaching_style1      enum ('CARING', 'CONFIDENCE_BOOSTER', 'ERROR', 'FOCUSED', 'FUN', 'METICULOUS', 'PASSIONATE') not null,
    teaching_style2      enum ('CARING', 'CONFIDENCE_BOOSTER', 'ERROR', 'FOCUSED', 'FUN', 'METICULOUS', 'PASSIONATE') not null,
    teaching_style_info1 text                                                                                         not null,
    teaching_style_info2 text                                                                                         not null,
    birth                varchar(255)                                                                                 not null,
    email                varchar(255)                                                                                 not null,
    gender               enum ('남', '여')                                                                              not null,
    name                 varchar(255)                                                                                 not null,
    nick_name            varchar(255)                                                                                 not null,
    phone_number         varchar(255)                                                                                 not null,
    profile              varchar(255)                                                                                 null,
    video                varchar(255)                                                                                 null,
    high_school          varchar(255)                                                                                 not null,
    high_school_type     varchar(255)                                                                                 not null,
    major                varchar(255)                                                                                 not null,
    university           varchar(255)                                                                                 not null,
    terminate_issue      text                                                                                         null,
    constraint UKn060cb5j4257tbjkw607v3f6g
    unique (phone_number)
    );

create table if not exists yedu.class_matching
(
    class_matching_id                      bigint auto_increment
    primary key,
    created_at                           datetime(6)                   null,
    updated_at                           datetime(6)                   null,
    match_status                         enum ('거절', '대기', '수락', '전송') null,
    refuse_reason                        varchar(255)                  null,
    response                             bit                           not null,
    response_time                        int                           not null,
    application_form_application_form_id varchar(255)                  null,
    teacher_teacher_id                   bigint                        null,
    constraint FKls7sswdmqgciks00rkjqoixo6
    foreign key (application_form_application_form_id) references yedu.application_form (application_form_id),
    constraint FKrb1jb15jjb7pns1x68r28btw0
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_available
(
    available_id       bigint auto_increment
    primary key,
    available_time     time(6)                                  not null,
    day                enum ('금', '목', '수', '월', '일', '토', '화') not null,
    teacher_teacher_id bigint                                   null,
    constraint FKbudvjpq2cb1i6ahsr0g3pk6lo
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_district
(
    district_id        bigint auto_increment
    primary key,
    district           enum ('ERROR', '강남구', '강동구', '강북구', '강서구', '경기지역', '고양', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '분당', '서대문구', '서초구', '성동구', '성북구', '송파구', '안양', '양천구', '영등포구', '온라인', '용산구', '용인', '은평구', '인천', '종로구', '중구', '중랑구') not null,
    teacher_teacher_id bigint                                                                                                                                                                                                                                       null,
    constraint FKf1hjhkynppv6kdob6tfwwkdko
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_english
(
    english_id          bigint auto_increment
    primary key,
    appeal_point        text   not null,
    foreign_experience  text   null,
    management_style    text   not null,
    teaching_experience text   not null,
    teaching_history    int    not null,
    teaching_style      text   not null,
    teacher_teacher_id  bigint null,
    constraint FKt7o0686li2wkthuf8qibtr11r
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

create table if not exists yedu.teacher_math
(
    math_id             bigint auto_increment
    primary key,
    appeal_point        text   not null,
    management_style    text   not null,
    teaching_experience text   not null,
    teaching_history    int    not null,
    teaching_style      text   not null,
    teacher_teacher_id  bigint null,
    constraint FK84pjet9p2vlfubiqi89dg6uah
    foreign key (teacher_teacher_id) references yedu.teacher (teacher_id)
    );

