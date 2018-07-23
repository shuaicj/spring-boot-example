-- users
-----------------------

-- <'admin', bcrypt('admin')>
insert into users(username, password) values ('admin', '$2a$10$ZR8oW1C2suzegIfp90ydQuDvdttD4caDf.lUGWcV8RKy1k3v8L9/G');
-- <'shuaicj', bcrypt('shuaicj')>
insert into users(username, password) values ('shuaicj', '$2a$10$zwyygCfFb2denM.glxDLGOq5xy8ZgjSQz.x4sZOdTUJ8XUpB/TZi2');


-- roles of users
-----------------------
insert into authorities(username, authority) values ('admin', 'ROLE_ADMIN');
insert into authorities(username, authority) values ('admin', 'ROLE_USER');
insert into authorities(username, authority) values ('shuaicj', 'ROLE_USER');
