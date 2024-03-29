create table if not exists appuser(
    id integer not null generated by default as identity PRIMARY KEY,
    last_name varchar(255),
    first_name varchar(255),
    middle_name varchar(255),
    birthday date,
    username varchar(100) unique not null,
    password varchar(100) not null,
    balance numeric(10, 2) check (balance >= 0),
    first_balance numeric(10, 2),
    account_nonexpired boolean,
    account_nonlocked boolean,
    credentials_nonexpired boolean,
    is_enabled boolean
);

create table if not exists phone(
    id integer not null generated by default as identity PRIMARY KEY,
    number varchar (100) unique,
    appuser_id integer,
    constraint fk_appuser foreign key (appuser_id) references appuser (id)
);

create table if not exists email(
    id integer not null generated by default as identity PRIMARY KEY,
    email varchar (100) unique,
    appuser_id integer,
    constraint fk_appuser foreign key (appuser_id) references appuser (id)
);
