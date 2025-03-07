CREATE TABLE IF NOT EXISTS `member`
(
    `id` bigint(20) NOT NULL,
    `user_id` varchar(255) NOT NULL UNIQUE,
    `user_name` varchar(255) DEFAULT NULL,
    `name_tag` varchar(255) DEFAULT NULL
    `password` varchar(255) NOT NULL,
    `role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
    `tier` varchar(255) DEFAULT NULL,
    `isActive` tinyint(1) NOT NULL DEFAULT 1,
    `refresh_token` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

