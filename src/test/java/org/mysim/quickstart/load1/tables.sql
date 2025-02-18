create table tmp.action_log
(
    action_id     int auto_increment
        primary key,
    turn          int         null,
    actor_id      varchar(50) null,
    action_name   varchar(50) null,
    property_desc text        null,
    status_desc   text        null
);

create table tmp.delivery_order
(
    id             int auto_increment
        primary key,
    generated_time datetime    null,
    finish_time    datetime    null,
    status         varchar(50) null
);

