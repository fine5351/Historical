-- Create users
CREATE USER IF NOT EXISTS root PASSWORD '123456';
CREATE USER IF NOT EXISTS pkuo PASSWORD '123456';
GRANT ALL ON SCHEMA PUBLIC TO root;
GRANT ALL ON SCHEMA PUBLIC TO pkuo;

drop table if exists skill;
create table skill (
    id bigint primary key auto_increment,
    name       nvarchar(255) not null,
    created_at datetime      not null default current_timestamp,
    updated_at datetime      not null default current_timestamp
);

drop table if exists team;
create table team (
    id bigint primary key auto_increment,
    name       varchar(255) not null,
    created_at datetime     not null default current_timestamp,
    updated_at datetime     not null default current_timestamp
);

drop table if exists allowed_skill_mapping;
create table allowed_skill_mapping (
    id bigint primary key auto_increment,
    team_id bigint not null,
    vacancy_id bigint   not null,
    skill_id bigint not null,
    skill_level int not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    unique (team_id, skill_id, skill_level)
);

drop table if exists resume;
create table resume
(
  id         bigint primary key auto_increment,
  file_name  varchar(255) not null,
  status     int          not null default 0,
  created_at datetime     not null default current_timestamp,
  updated_at datetime     not null default current_timestamp
);

drop table if exists vacancy;
create table vacancy
(
  id         bigint primary key auto_increment,
  title      varchar(255) not null,
  created_at datetime     not null default current_timestamp,
  updated_at datetime     not null default current_timestamp
);
