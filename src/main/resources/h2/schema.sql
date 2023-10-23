drop table if exists skill;
create table skill (
    id bigint primary key auto_increment,
    name nvarchar(255) not null
);

drop table if exists team;
create table team (
    id bigint primary key auto_increment,
    name varchar(255) not null
);

drop table if exists allowed_skill_mapping;
create table allowed_skill_mapping (
    id bigint primary key auto_increment,
    team_id bigint not null,
    skill_id bigint not null,
    skill_level int not null,
    unique (team_id, skill_id, skill_level)
);