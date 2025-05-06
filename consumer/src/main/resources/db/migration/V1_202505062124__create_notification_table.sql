create table push.notification (
      id bigint not null auto_increment,
      consumed_at datetime(6),
      content varchar(5000),
      server_key varchar(255),
      client_key varchar(255),
      receiver_phone_number varchar(255),
      push_type enum ('BIZPURRIO_KAKAO_ALARMTALK'),
      receiver_type enum ('PARENT','TEACHER'),
      status enum ('IN_PROGRESS','FAIL','SUCCESS','DELIVERED'),
      primary key (id)
);

CREATE INDEX idx_server_client_key ON push.notification (server_key, client_key);
