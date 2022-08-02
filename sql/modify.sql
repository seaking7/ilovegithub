
insert into g_user(id, login, type, status)
select distinct git_id, login, type, 'INIT' from g_gituser
where git_id > 109630;

insert into g_user(id, login, type, status)
select distinct git_id, login, type, 'INIT'  from g_gituser where git_id > 6262911;


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
or blog like '%tistory%');

select * from g_user where is_korean = true;

update g_user set is_korean = true where is_korean = false
and (lower(location) like '%korea%' or lower(location) like '%seoul%'
or blog like '%tistory%');

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


insert into g_source_rank(id, login, name, size, stargazers_count, language, is_korean, created_at, updated_at, pushed_at)
select id, login, name, size, stargazers_count, language,
    (select is_korean from g_user a where a.id = gr.user_id ) is_korean,
    created_at, updated_at , pushed_at
from g_repository gr
order by stargazers_count desc;


insert into g_source_rank(id, login, name, size, stargazers_count, language, is_korean, created_at, updated_at, pushed_at)
select id, login, name, size, stargazers_count, language,
    (select is_korean from g_user a where a.id = gr.user_id ) is_korean,
    created_at, updated_at , pushed_at
from g_repository gr
where stargazers_count  > 100
order by stargazers_count desc;
