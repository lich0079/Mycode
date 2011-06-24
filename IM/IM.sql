use mydb
go

create table userinfo
(
	id varchar(20) primary key,
	pw varchar(10) not null,
	ip varchar(20),
	online bit, 
)
go

insert into userinfo values('10000','10000','127.0.0.1',0)
insert into userinfo values('10001','10001','127.0.0.1',0)
insert into userinfo values('10002','10002','127.0.0.1',0)
insert into userinfo values('10003','10003','127.0.0.1',0)
insert into userinfo values('10004','10004','127.0.0.1',0)
go

select * from userinfo
go

create table friendlist_10001
(
	id varchar(20) primary key,		
)
go
insert into friendlist_10001 values('10002')
insert into friendlist_10001 values('10003')
insert into friendlist_10001 values('10004')
go
select * from friendlist_10001
go
create table friendlist_10002
(
	id varchar(20) primary key,		
)
go
insert into friendlist_10002 values('10001')
insert into friendlist_10002 values('10003')
insert into friendlist_10002 values('10004')
go
select * from friendlist_10002
go

create table friendlist_10003
(
	id varchar(20) primary key,		
)
go
insert into friendlist_10003 values('10001')
insert into friendlist_10003 values('10002')
insert into friendlist_10003 values('10004')
go
select * from friendlist_10003
go

create table friendlist_10004
(
	id varchar(20) primary key,		
)
go
insert into friendlist_10004 values('10001')
insert into friendlist_10004 values('10003')
insert into friendlist_10004 values('10002')
go
select * from friendlist_10004
go