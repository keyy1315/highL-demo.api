CREATE TABLE IF NOT EXISTS `member`
(
    `id` bigint(20) NOT NULL,
    `email` varchar(255) NOT NULL UNIQUE ,
    `password` varchar(255) DEFAULT NULL,
    `role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
    `tier` varchar(255) DEFAULT NULL,
    `isActive` tinyint(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`)
);

