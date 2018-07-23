drop table if exists users;

create table users (
    id bigint primary key auto_increment,
    username varchar(100) not null unique,
    password varchar(100) not null,
    enabled boolean not null default true
);

drop table if exists authorities;

create table authorities (
    id bigint primary key auto_increment,
    username varchar(100) not null,
    authority varchar(100) not null
);
