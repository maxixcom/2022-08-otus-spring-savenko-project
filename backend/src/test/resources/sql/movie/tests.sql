insert into t_category(id, title)
values (1, 'title'),
       (2, 'xxx');

insert into t_movie(id,title,description,rating_count,rating_sum,rating,embed_code,category_id,has_poster)
values (1,'title_1','description_1',0,0,0,'<iframe></iframe>',1,false),
       (2,'title_2','description_2',0,0,0,'<iframe></iframe>',2,false);
