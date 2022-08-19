   CREATE TABLE `g_gituser5` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `login` varchar(100) NOT NULL,
    `git_id` int(11) NOT NULL,
    `type` varchar(100) NOT NULL,
    PRIMARY KEY (`id`));


CREATE TABLE `g_member_history` (
   `seq` bigint NOT NULL AUTO_INCREMENT,
   id int NOT NULL,
   `login` varchar(255) NOT NULL,
   `login_at` datetime(6) DEFAULT (curtime()),
   PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `g_rank_job_index` (
   `id` int NOT NULL AUTO_INCREMENT,
   `rank_table` varchar(255) NOT NULL,
   `from_id` int DEFAULT NULL,
   `to_id` int DEFAULT NULL,
   `created_at` datetime(6) DEFAULT (current_time),
   `updated_at` datetime(6) DEFAULT (current_time),
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


     CREATE TABLE `g_user` (
    `id` int(11) NOT NULL,
   `login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
   status varchar(200)default 'INIT',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `blog` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bio` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `public_repos` int DEFAULT NULL,
  `public_gists` int DEFAULT NULL,
  `followers` int DEFAULT NULL,
  `following` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
    `fetched_on` datetime(6) DEFAULT (current_time),
    PRIMARY KEY (`id`))
   partition by range(id)(
  	partition p0 values less than (1000000),
  	partition p1 values less than (2000000),
  	partition p2 values less than (3000000),
  	partition p3 values less than (4000000),
  	partition p4 values less than (5000000),
  	partition p5 values less than (6000000),
  	partition p6 values less than (7000000),
  	partition p7 values less than (8000000),
  	partition p8 values less than (9000000),
  	partition p9 values less than (10000000),
  	partition p10 values less than MAXVALUE);


   alter table g_user add partition ( partition p20 VALUES LESS THAN (20000000));
   alter table g_user add partition ( partition p99 VALUES LESS THAN MAXVALUE);
   alter table g_user drop  PARTITION p10;

alter table g_user drop PARTITION p99;

alter table g_user add partition ( partition p31 VALUES LESS THAN (31000000));

alter table g_user add partition ( partition p32 VALUES LESS THAN (32000000));

alter table g_user add partition ( partition p33 VALUES LESS THAN (33000000));

alter table g_user add partition ( partition p34 VALUES LESS THAN (34000000));

alter table g_user add partition ( partition p35 VALUES LESS THAN (35000000));

alter table g_user add partition ( partition p36 VALUES LESS THAN (36000000));

alter table g_user add partition ( partition p37 VALUES LESS THAN (37000000));

alter table g_user add partition ( partition p38 VALUES LESS THAN (38000000));

alter table g_user add partition ( partition p39 VALUES LESS THAN (39000000));

alter table g_user add partition ( partition p40 VALUES LESS THAN (40000000));

alter table g_user add partition ( partition p99 VALUES LESS THAN MAXVALUE);

   CREATE TABLE `g_user` (
  `id` int NOT NULL,
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(200) DEFAULT 'INIT',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `public_repos` int DEFAULT NULL,
  `public_gists` int DEFAULT NULL,
  `followers` int DEFAULT NULL,
  `following` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `fetched_on` datetime(6) DEFAULT (current_time),
  PRIMARY KEY (`id`)NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

alter table g_user add is_korean boolean default false;


CREATE TABLE `g_member` (
   `id` int NOT NULL,
   `login` varchar(255) NOT NULL,
   `status` varchar(200) DEFAULT 'INIT',
   `name` varchar(255) DEFAULT NULL,
   `role` varchar(255) NOT NULL,
   `type` varchar(255) NOT NULL,
   `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `public_repos` int DEFAULT 0,
   `followers` int DEFAULT 0,
   `following` int DEFAULT 0,
   `created_at` datetime(6) DEFAULT NULL,
   `updated_at` datetime(6) DEFAULT NULL,
   joined_at datetime(6) DEFAULT (curtime()),
   last_login_at datetime(6) DEFAULT (curtime()),
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `g_question` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `login` varchar(255) NOT NULL,
 `name` varchar(255) DEFAULT NULL,
 `content` varchar(10000) DEFAULT NULL,
 `content_type` varchar(255) DEFAULT NULL,
 `created_at` datetime(6) DEFAULT NULL,
 `updated_at` datetime(6) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `g_repository` (
  `id` int NOT NULL,
  `login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `size` int DEFAULT NULL,
  `stargazers_count` int DEFAULT NULL,
  `language` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `pushed_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create index g_repository_idx1 on g_repository(login);

alter table g_repository add user_id int  DEFAULT null after login;


CREATE TABLE `g_user_rank` (
  `id` int NOT NULL,
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `followers` int DEFAULT NULL,
  `following` int DEFAULT NULL,
  `size` int DEFAULT '0',
  `stargazers_count` int DEFAULT '0',
  `first_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `second_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `third_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_korean` tinyint(1) DEFAULT '0',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `g_user_rank_result` (
 `id` int NOT NULL,
 `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
 `followers` int DEFAULT NULL,
 `following` int DEFAULT NULL,
 `size` int DEFAULT '0',
 `stargazers_count` int DEFAULT '0',
 `first_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
 `second_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
 `third_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
 `is_korean` tinyint(1) DEFAULT '0',
 `created_at` datetime(6) DEFAULT NULL,
 `updated_at` datetime(6) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `g_org_rank` (
     `id` int NOT NULL,
     `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
     `people_count` int DEFAULT '0',
     `size` int DEFAULT '0',
     `stargazers_count` int DEFAULT '0',
     `first_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     `second_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     `third_language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     `is_korean` tinyint(1) DEFAULT '0',
     `created_at` datetime(6) DEFAULT NULL,
     `updated_at` datetime(6) DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `g_source_rank` (
    `id` int NOT NULL,
    `login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `size` int DEFAULT 0,
    `stargazers_count` int DEFAULT 0,
    `language` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `pushed_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

   alter table g_source_rank add is_korean boolean default false after language;


CREATE TABLE `g_org_members` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` int NOT NULL,
    `member_login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `org_id` int NOT NULL,
    `org_login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



CREATE TABLE `g_repository` (
           `id` int NOT NULL,
           `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
           `user_id` int DEFAULT NULL,
           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
           `size` int DEFAULT NULL,
           `stargazers_count` int DEFAULT NULL,
           `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
           `created_at` datetime(6) DEFAULT NULL,
           `updated_at` datetime(6) DEFAULT NULL,
           `pushed_at` datetime(6) DEFAULT NULL,
           `fetched_on` datetime(6) DEFAULT NULL,
           PRIMARY KEY (`id`),
           KEY `g_repository_idx1` (`login`)
   ) partition by range(id)(
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
  	partition p10 values less than (110000000),
  	partition p11 values less than (120000000),
  	partition p12 values less than (130000000),
  	partition p13 values less than (140000000),
  	partition p14 values less than (150000000),
  	partition p15 values less than (160000000),
  	partition p16 values less than (170000000),
  	partition p17 values less than (180000000),
  	partition p18 values less than (190000000),
  	partition p19 values less than (200000000),
  	partition p20 values less than (210000000),
  	partition p21 values less than (220000000),
  	partition p22 values less than (230000000),
  	partition p23 values less than (240000000),
  	partition p24 values less than (250000000),
  	partition p25 values less than (260000000),
  	partition p26 values less than (270000000),
  	partition p27 values less than (280000000),
  	partition p28 values less than (290000000),
  	partition p29 values less than (300000000),
  	partition p30 values less than (310000000),
  	partition p31 values less than (320000000),
  	partition p32 values less than (330000000),
  	partition p33 values less than (340000000),
  	partition p34 values less than (350000000),
  	partition p35 values less than (360000000),
  	partition p36 values less than (370000000),
  	partition p37 values less than (380000000),
  	partition p38 values less than (390000000),
  	partition p39 values less than (400000000),
  	partition p40 values less than (410000000),
  	partition p41 values less than (420000000),
  	partition p42 values less than (430000000),
  	partition p43 values less than (440000000),
  	partition p44 values less than (450000000),
  	partition p45 values less than (460000000),
  	partition p46 values less than (470000000),
  	partition p47 values less than (480000000),
  	partition p48 values less than (490000000),
  	partition p49 values less than (500000000),
  	partition p50 values less than (510000000),
  	partition p51 values less than (520000000),
  	partition p52 values less than (530000000),
  	partition p53 values less than (540000000),
  	partition p54 values less than (550000000),
  	partition p55 values less than (560000000),
  	partition p56 values less than (570000000),
  	partition p57 values less than (580000000),
  	partition p58 values less than (590000000),
  	partition p59 values less than (600000000),
  	partition p60 values less than (610000000),
  	partition p61 values less than (620000000),
  	partition p62 values less than (630000000),
  	partition p63 values less than (640000000),
  	partition p64 values less than (650000000),
  	partition p65 values less than (660000000),
  	partition p66 values less than (670000000),
  	partition p67 values less than (680000000),
  	partition p68 values less than (690000000),
  	partition p69 values less than (700000000),
  	partition p70 values less than (710000000),
  	partition p71 values less than (720000000),
  	partition p72 values less than (730000000),
  	partition p73 values less than (740000000),
  	partition p74 values less than (750000000),
  	partition p75 values less than (760000000),
  	partition p76 values less than (770000000),
  	partition p77 values less than (780000000),
  	partition p78 values less than (790000000),
  	partition p79 values less than (800000000),
  	partition p80 values less than (810000000),
  	partition p81 values less than (820000000),
  	partition p82 values less than (830000000),
  	partition p83 values less than (840000000),
  	partition p84 values less than (850000000),
  	partition p85 values less than (860000000),
  	partition p86 values less than (870000000),
  	partition p87 values less than (880000000),
  	partition p88 values less than (890000000),
  	partition p89 values less than (900000000),
  	partition p90 values less than (910000000),
  	partition p91 values less than (920000000),
  	partition p92 values less than (930000000),
  	partition p93 values less than (940000000),
  	partition p94 values less than (950000000),
  	partition p95 values less than (960000000),
  	partition p96 values less than (970000000),
  	partition p97 values less than (980000000),
  	partition p98 values less than (990000000),
  	partition p99 values less than (1000000000),
    PARTITION p999 VALUES LESS THAN MAXVALUE );