   CREATE TABLE `g_gituser5` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `login` varchar(100) NOT NULL,
    `git_id` int(11) NOT NULL,
    `type` varchar(100) NOT NULL,
    PRIMARY KEY (`id`));

  
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


CREATE TABLE `g_user_rank` (
`id` int NOT NULL,
`login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
`followers` int DEFAULT NULL,
`following` int DEFAULT NULL,
`size` int DEFAULT NULL,
`stargazers_count` int DEFAULT NULL,
`main_language` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
is_korean boolean default false,
`created_at` datetime(6) DEFAULT NULL,
`updated_at` datetime(6) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;