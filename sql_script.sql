use foodproof_users;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_at` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10; 

alter table `users` add constraint `name` unique (`name`); 
alter table `users` add constraint `email` unique (`email`); 

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `user` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `created_at` datetime,
   KEY `token` (`token`)
)ENGINE=InnoDB;
