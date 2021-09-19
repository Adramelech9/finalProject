create sequence hibernate_sequence start 1 increment 1;

create table table_users (id varchar(255) not null,
first_entry timestamp,
is_active boolean not null,
last_entry timestamp,
social_network varchar(255),
user_name varchar(255),
primary key (id));
