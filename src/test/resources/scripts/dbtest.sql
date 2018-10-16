drop schema if exists `computer-database-db`;
create schema if not exists `computer-database-db`;
use `computer-database-db`;

drop table if exists computer;
drop table if exists company;

create table company (
  id                        bigint not null auto_increment,
  name                      varchar(255),
  constraint pk_company primary key (id))
;

create table computer (
  id                        bigint not null auto_increment,
  name                      varchar(255),
  introduced                date NULL,
  discontinued              date NULL,
  company_id                bigint default NULL,
  constraint pk_computer primary key (id));

alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_1 on computer (company_id);

CREATE USER IF NOT EXISTS ADMINCDBTEST PASSWORD 'qwerty1234';
GRANT ALL TO ADMINCDBTEST;

insert into company (id,name) values (  1,'Apple Inc.');
insert into company (id,name) values (  2,'Thinking Machines');

insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook1',null,null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 2,'MacBook2','1980-05-01','1984-04-01',1);
insert into computer (id,name,introduced,discontinued,company_id) values (  3,'TM',null,null,2);

