# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table channel (
  CHANNEL_ID                bigint not null,
  name                      varchar(255) not null,
  creation_date             timestamp not null,
  genre_genre_id            bigint,
  constraint pk_channel primary key (CHANNEL_ID))
;

create table episode (
  episode_id                bigint not null,
  name                      varchar(255) not null,
  author                    varchar(255),
  show_show_id              bigint,
  description               TEXT,
  link                      varchar(2000),
  audio_url                 varchar(2000) not null,
  duration                  bigint,
  published_date            timestamp,
  explicit                  boolean not null,
  creation_date             timestamp not null,
  constraint pk_episode primary key (episode_id))
;

create table genre (
  genre_id                  bigint not null,
  name                      varchar(256) not null,
  description               TEXT,
  parent_genre_genre_id     bigint,
  apple_genre_id            bigint,
  creation_date             timestamp not null,
  constraint uq_genre_name unique (name),
  constraint pk_genre primary key (genre_id))
;

create table network (
  NETWORK_ID                bigint not null,
  name                      varchar(255) not null,
  website_url               varchar(255),
  profile_image_url         varchar(255),
  header_image_url          varchar(255),
  description               varchar(255),
  creation_date             timestamp not null,
  constraint uq_network_name unique (name),
  constraint pk_network primary key (NETWORK_ID))
;

create table security_role (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_security_role primary key (id))
;

create table show (
  show_id                   bigint not null,
  show_type                 integer not null,
  network_NETWORK_ID        bigint,
  name                      varchar(255) not null,
  author                    varchar(255),
  description               TEXT,
  rights_label              varchar(255),
  rss_feed_url              varchar(2000),
  website_url               varchar(2000),
  hosts_display_name        varchar(255),
  i_tunes_link_url          varchar(2000),
  i_tunes_id                bigint,
  image_url                 varchar(2000),
  language_code             varchar(255),
  explicit                  boolean not null,
  creation_date             timestamp not null,
  constraint ck_show_show_type check (show_type in (0,1,2)),
  constraint uq_show_rss_feed_url unique (rss_feed_url),
  constraint pk_show primary key (show_id))
;

create table show_list (
  id                        bigint not null,
  constraint pk_show_list primary key (id))
;

create table tag (
  tag_id                    bigint not null,
  name                      varchar(256) not null,
  creation_date             timestamp not null,
  constraint uq_tag_name unique (name),
  constraint pk_tag primary key (tag_id))
;

create table tag_list (
  id                        bigint not null,
  constraint pk_tag_list primary key (id))
;

create table app_user (
  id                        bigint not null,
  email_address             varchar(256) not null,
  sha_password              varbinary(64) not null,
  first_name                varchar(256),
  last_name                 varchar(256),
  zip_code                  bigint,
  creation_date             timestamp not null,
  user_type                 integer not null,
  status                    integer not null,
  auth_token                varchar(255),
  identifier                varchar(255),
  constraint ck_app_user_user_type check (user_type in (0,1,2)),
  constraint ck_app_user_status check (status in (0,1)),
  constraint uq_app_user_email_address unique (email_address),
  constraint pk_app_user primary key (id))
;

create table user_list (
  id                        bigint not null,
  constraint pk_user_list primary key (id))
;

create table user_permission (
  id                        bigint not null,
  permission_value          varchar(255),
  constraint pk_user_permission primary key (id))
;


create table channel_show (
  channel_CHANNEL_ID             bigint not null,
  show_show_id                   bigint not null,
  constraint pk_channel_show primary key (channel_CHANNEL_ID, show_show_id))
;

create table episode_tag (
  episode_episode_id             bigint not null,
  tag_tag_id                     bigint not null,
  constraint pk_episode_tag primary key (episode_episode_id, tag_tag_id))
;

create table show_genre (
  show_show_id                   bigint not null,
  genre_genre_id                 bigint not null,
  constraint pk_show_genre primary key (show_show_id, genre_genre_id))
;

create table show_list_show (
  show_list_id                   bigint not null,
  show_show_id                   bigint not null,
  constraint pk_show_list_show primary key (show_list_id, show_show_id))
;

create table tag_list_tag (
  tag_list_id                    bigint not null,
  tag_tag_id                     bigint not null,
  constraint pk_tag_list_tag primary key (tag_list_id, tag_tag_id))
;

create table user_list_app_user (
  user_list_id                   bigint not null,
  app_user_id                    bigint not null,
  constraint pk_user_list_app_user primary key (user_list_id, app_user_id))
;
create sequence channel_seq;

create sequence episode_seq;

create sequence genre_seq;

create sequence network_seq;

create sequence security_role_seq;

create sequence show_seq;

create sequence show_list_seq;

create sequence tag_seq;

create sequence tag_list_seq;

create sequence app_user_seq;

create sequence user_list_seq;

create sequence user_permission_seq;

alter table channel add constraint fk_channel_genre_1 foreign key (genre_genre_id) references genre (genre_id) on delete restrict on update restrict;
create index ix_channel_genre_1 on channel (genre_genre_id);
alter table episode add constraint fk_episode_show_2 foreign key (show_show_id) references show (show_id) on delete restrict on update restrict;
create index ix_episode_show_2 on episode (show_show_id);
alter table genre add constraint fk_genre_parentGenre_3 foreign key (parent_genre_genre_id) references genre (genre_id) on delete restrict on update restrict;
create index ix_genre_parentGenre_3 on genre (parent_genre_genre_id);
alter table show add constraint fk_show_network_4 foreign key (network_NETWORK_ID) references network (NETWORK_ID) on delete restrict on update restrict;
create index ix_show_network_4 on show (network_NETWORK_ID);



alter table channel_show add constraint fk_channel_show_channel_01 foreign key (channel_CHANNEL_ID) references channel (CHANNEL_ID) on delete restrict on update restrict;

alter table channel_show add constraint fk_channel_show_show_02 foreign key (show_show_id) references show (show_id) on delete restrict on update restrict;

alter table episode_tag add constraint fk_episode_tag_episode_01 foreign key (episode_episode_id) references episode (episode_id) on delete restrict on update restrict;

alter table episode_tag add constraint fk_episode_tag_tag_02 foreign key (tag_tag_id) references tag (tag_id) on delete restrict on update restrict;

alter table show_genre add constraint fk_show_genre_show_01 foreign key (show_show_id) references show (show_id) on delete restrict on update restrict;

alter table show_genre add constraint fk_show_genre_genre_02 foreign key (genre_genre_id) references genre (genre_id) on delete restrict on update restrict;

alter table show_list_show add constraint fk_show_list_show_show_list_01 foreign key (show_list_id) references show_list (id) on delete restrict on update restrict;

alter table show_list_show add constraint fk_show_list_show_show_02 foreign key (show_show_id) references show (show_id) on delete restrict on update restrict;

alter table tag_list_tag add constraint fk_tag_list_tag_tag_list_01 foreign key (tag_list_id) references tag_list (id) on delete restrict on update restrict;

alter table tag_list_tag add constraint fk_tag_list_tag_tag_02 foreign key (tag_tag_id) references tag (tag_id) on delete restrict on update restrict;

alter table user_list_app_user add constraint fk_user_list_app_user_user_li_01 foreign key (user_list_id) references user_list (id) on delete restrict on update restrict;

alter table user_list_app_user add constraint fk_user_list_app_user_app_use_02 foreign key (app_user_id) references app_user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists channel;

drop table if exists channel_show;

drop table if exists episode;

drop table if exists episode_tag;

drop table if exists genre;

drop table if exists network;

drop table if exists security_role;

drop table if exists show;

drop table if exists show_genre;

drop table if exists show_list_show;

drop table if exists show_list;

drop table if exists tag;

drop table if exists tag_list;

drop table if exists tag_list_tag;

drop table if exists app_user;

drop table if exists user_list_app_user;

drop table if exists user_list;

drop table if exists user_permission;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists channel_seq;

drop sequence if exists episode_seq;

drop sequence if exists genre_seq;

drop sequence if exists network_seq;

drop sequence if exists security_role_seq;

drop sequence if exists show_seq;

drop sequence if exists show_list_seq;

drop sequence if exists tag_seq;

drop sequence if exists tag_list_seq;

drop sequence if exists app_user_seq;

drop sequence if exists user_list_seq;

drop sequence if exists user_permission_seq;

