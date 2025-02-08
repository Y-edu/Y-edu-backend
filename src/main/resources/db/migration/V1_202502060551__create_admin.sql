create table if not exists yedu.admin
(
    admin_id bigint auto_increment
    primary key,
    password varchar(255) null,
    login_id varchar(255) null
    );
