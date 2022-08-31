
insert into g_user(id, login, type, status)
select distinct git_id, login, type, 'INIT' from g_gituser
where git_id > 109630;

insert into g_user(id, login, type, status)
select distinct git_id, login, type, 'INIT'  from g_gituser where git_id > 17801101 and type != 'Bot';


insert into g_user_rank(id, login, type, followers, following, size, stargazers_count,
                        created_at, updated_at, is_korean)
select a.id, a.login, a.type, a.followers, a.following,
       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,
a.created_at, a.updated_at, a.is_korean
from g_user a, g_repository gr
where a.status = 'REPO_INSERTED'
  and a.login = gr.login
    -- and a.id <=50000
group by a.id, a.login, a.type, a.followers, a.following, a.created_at, a.updated_at, a.is_korean;


select * from g_user where is_korean = false
   and (lower(location) like '%korea%' or lower(location) like '%seoul%'
or lower(blog) like '%tistory.com%' or lower(blog) like '%naver.com%'
or lower(email) like '%hanmail.net%' or lower(email) like '%naver.com%');

select * from g_user where is_korean = true;

update g_user set is_korean = true where is_korean = false
     and (lower(location) like '%korea%' or lower(location) like '%seoul%'
or lower(blog) like '%tistory.com%' or lower(blog) like '%naver.com%'
or lower(email) like '%hanmail.net%' or lower(email) like '%naver.com%');


create table korean_tmp as
select * from g_user where login in (
    select member_login from g_org_members where org_login in (
        select login from g_user where is_korean is true and type = 'Organization')) and is_korean is false;

delete from  g_user where id > 1 and id < 7000000 and status = 'NOT_FOUND'

update g_user b  inner join g_repository a  on b.login = a.login set a.user_id = b.id;

insert into g_user_rank(id, login, followers, following, size, stargazers_count,
                        created_at, updated_at, is_korean)
select a.id, a.login, a.followers, a.following,
       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,
a.created_at, a.updated_at, a.is_korean
from g_user a, g_repository gr
where a.status = 'REPO_INSERTED'
  and a.type = 'User'
  and a.login = gr.login
    -- and a.id <=50000
group by a.id, a.login, a.followers, a.following, a.created_at, a.updated_at, a.is_korean;


insert into g_user_rank(id, login, followers, following, size, stargazers_count,
                        created_at, updated_at, is_korean)
select a.id, a.login, a.followers, a.following,
       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,
a.created_at, a.updated_at, a.is_korean
from g_user a, g_repository gr
where a.status = 'REPO_INSERTED'
  and a.type = 'User'
  and a.login = gr.login
  and a.id > 1500000  and a.id <= 1800000
group by a.id, a.login, a.followers, a.following, a.created_at, a.updated_at, a.is_korean;

insert into g_org_rank(id, login, people_count, size, stargazers_count,
                       created_at, updated_at, is_korean)
select a.id, a.login,
       (select count(*) count from g_org_members gom2 where gom2.org_id = a.id) people_count,
    sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,
    a.created_at, a.updated_at, a.is_korean
from g_user a, g_repository gr
where a.status = 'ORG_INSERTED'
  and a.type = 'Organization'
  and a.login = gr.login
     and a.id <=50000
group by a.id, a.login, a.created_at, a.updated_at, a.is_korean;


insert into g_source_rank_tmp(id, login, user_id, name, size, stargazers_count, language, is_korean, created_at, updated_at, pushed_at)
select id, login, user_id, name, size, stargazers_count, language,
    (select is_korean from g_user a where a.id = gr.user_id ) is_korean,
    created_at, updated_at , pushed_at
from g_repository gr
where stargazers_count  > 1000
order by stargazers_count desc;

truncate table g_source_rank;

insert into g_source_rank(id, login, user_id, name, size, stargazers_count, language, is_korean, created_at, updated_at, pushed_at)
select id, login, user_id, name, size, stargazers_count, language, is_korean, created_at, updated_at, pushed_at
from g_source_rank_tmp;


select date_format(start_time, '%Y%m%d'), step_name, sum(WRITE_COUNT) from BATCH_STEP_EXECUTION bse
group by date_format(start_time, '%Y%m%d'), step_name
order by date_format(start_time, '%Y%m%d') desc;



insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 4500000, 5000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 5000000, 5500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 5500000, 6000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 6000000, 6500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 6500000, 7000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 7000000, 7500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 7500000, 8000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 8000000, 8500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 8500000, 9000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 10000000, 10500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 10500000, 11000000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 11000000, 11500000);

insert into g_rank_job_index (rank_table, from_id, to_id) values( 'g_user_rank', 11500000, 12000000);