create table users(
	id varchar(10) primary key, 
	name varchar(20) not null, 
	password varchar(10) not null, 
	level tinyint(3) not null, 
	login int(3) not null, 
	recommend int(3) not null
)