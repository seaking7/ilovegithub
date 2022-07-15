
insert into g_user(id, login, type, status)
select distinct git_id, login, type, 'INIT' from g_gituser
where git_id > 109630;


insert into g_user_rank(id, login, followers, following, size, stargazers_count,
                        created_at, updated_at, is_korean)
select a.id, a.login, a.followers, a.following,
       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,
a.created_at, a.updated_at, a.is_korean
from g_user a, g_repository gr
where a.status = 'REPO_INSERTED'
  and a.login = gr.login
  and a.id <=50000
group by a.id, a.login, a.followers, a.following, a.created_at, a.updated_at, a.is_korean;



select * from g_user where is_korean = false
and (lower(location) like '%korea%' or lower(location) like '%seoul%'
or blog like '%tistory%');

select * from g_user where is_korean = true;

update g_user set is_korean = true where is_korean = false
and (lower(location) like '%korea%' or lower(location) like '%seoul%'
or blog like '%tistory%');