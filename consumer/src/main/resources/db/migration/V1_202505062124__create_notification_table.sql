create table push.notification (
      id bigint not null auto_increment,
      consumed_at datetime(6),
      content varchar(5000),
      push_key varchar(255),
      receiver_phone_number varchar(255),
      push_type enum ('BIZPURRIO_KAKAO_ALARMTALK'),
      receiver_type enum ('PARENT','TEACHER'),
      status enum ('FAIL','IN_PROGRESS','SUCCESS'),
      primary key (id)
);
