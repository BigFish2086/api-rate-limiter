drop schema if exists rate_limiter cascade;

create schema rate_limiter;

create function rate_limiter.update_modified_at()
returns trigger
language plpgsql
as
$$
begin
  new.modified_at = current_timestamp;
  return new;
end;
$$;

create table rate_limiter.membership_lookup (
  id integer constraint pk_privileges primary key,  
  membership_name varchar(64) not null unique,
  requests_per_second integer not null,
  rate_limit_window_seconds integer not null
);

insert into rate_limiter.membership_lookup (id, membership_name, requests_per_second, rate_limit_window_seconds) values 
(1, 'free', 10, 60),
(2, 'basic', 100, 60),
(3, 'premium', 1000, 60);

create table rate_limiter.users (
  id integer constraint pk_users primary key generated always as identity,
  username varchar(64) not null unique,
  email varchar(128) not null unique,
  password varchar(512) not null unique,
  membership_id integer not null default 1,
  created_at timestamp not null default current_timestamp,
  modified_at timestamp not null default current_timestamp,
  constraint fk_users_membership_id foreign key (membership_id) references rate_limiter.membership_lookup(id)
);
create trigger update_users_modified_at
before update on rate_limiter.users 
for each row 
execute function rate_limiter.update_modified_at();


