   CREATE TABLE `g_gituser5` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `login` varchar(100) NOT NULL,
    `git_id` int(11) NOT NULL,
    `type` varchar(100) NOT NULL,
    PRIMARY KEY (`id`));
   
   drop table g_gituser5;
    
  
     CREATE TABLE `g_user` (
    `id` int(11) NOT NULL,
    `login` varchar(100) NOT NULL,
    `type` varchar(100) NOT NULL,
    status varchar(20),
    PRIMARY KEY (`id`))
   partition by range(id)(
  	partition p0 values less than (10000000),
  	partition p1 values less than (20000000),
  	partition p2 values less than (30000000),
  	partition p3 values less than (40000000),
  	partition p4 values less than (50000000),
  	partition p5 values less than (60000000),
  	partition p6 values less than (70000000),
  	partition p7 values less than (80000000),
  	partition p8 values less than (90000000),
  	partition p9 values less than (100000000),
  	partition p10 values less than MAXVALUE);
  
  insert into g_user 
  select distinct git_id, login, type, 'INIT' from g_gituser
 where git_id >= 1000000;

select * from g_user where id > 999990;

select * from g_user order by id desc;

select count(*) from g_user;


commit;


set innodb_lock_wait_timeout=3000;

select * from g_user;
   
   
  select * from g_gituser_bak 
 where git_id > 259800 
order by git_id desc;

commit;

create table g_gituser_bak
as select * from g_gituser;

commit;

drop table g_gituser;

insert into g_gituser(login, git_id, type) 
select login, git_id, type from g_gituser_bak;

select * from g_gituser order by git_id desc;

select max(git_id) from g_gituser;

select count(*) from g_gituser;

select * from g_gituser2;

insert into g_gituser5(login, git_id, type)
values('tiagofoentes', 40000001, 'User');

commit;

select * from g_gituser order by git_id desc;
  

