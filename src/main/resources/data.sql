insert into users(id, join_date, name, password, ssn) values(90001, now(), 'user1', 'test1111', '111111-1111111');
insert into users(id, join_date, name, password, ssn) values(90002, now(), 'user2', 'test12111', '111111-1111111');
insert into users(id, join_date, name, password, ssn) values(90003, now(), 'user3', 'test11311', '111111-1111111');

insert into post(description, user_id) values('first post', 90001);
insert into post(description, user_id) values('second post', 90001);
