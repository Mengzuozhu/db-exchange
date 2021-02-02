drop table user if exists;

create table user
(
    id   bigint auto_increment,
    name varchar(20) not null,
    PRIMARY KEY (`id`)
);
