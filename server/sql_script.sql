use foodproof_users;

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

CREATE TABLE `sessions` (
  `user` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `created_at` datetime,
   KEY `token` (`token`)
)ENGINE=InnoDB;

CREATE TABLE `ingredient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `created_at` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2; 
alter table `ingredient` add constraint `name` unique (`name`); 

CREATE TABLE `user-ingredient` (
  `user_id` int(11) NOT NULL,
  `ingredient_id` int(11)  NOT NULL,
  `created_at` datetime,
  PRIMARY KEY (`user_id`,`ingredient_id`)
) ENGINE=InnoDB;

ALTER TABLE `user-ingredient`
  ADD CONSTRAINT `user_constraint` 
  FOREIGN KEY (`user_id`)
      REFERENCES `users`(`id`);

ALTER TABLE `user-ingredient`
    ADD CONSTRAINT `ingredient_constraint`
      FOREIGN KEY (`ingredient_id`) 
        REFERENCES `ingredient`(`id`);

CREATE TABLE `images`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name`varchar(255) DEFAULT NULL,
    `extension` varchar(5) DEFAULT NULL,
    `size` int(11) default 0,
    `md5` VARCHAR(32) NOT NULL,
    `created_at` datetime,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=2; 

CREATE TABLE `ingredient-images` (
   `ingredient_id` int(11),
  `image_id` int(11),
  `created_at` datetime,
  PRIMARY KEY (`ingredient_id`,`imaexit
  ge_id`)
) ;

ALTER TABLE `ingredient-images`
  ADD CONSTRAINT `ingredient_image_constraint` 
  FOREIGN KEY (`ingredient_id`)
      REFERENCES `ingredient`(`id`);

ALTER TABLE `ingredient-images`
    ADD CONSTRAINT `image_constraint`
      FOREIGN KEY (`image_id`) 
        REFERENCES `images`(`id`);
