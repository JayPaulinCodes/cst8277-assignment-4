use `acmecollege`;

-- Table for SecurityUser

CREATE TABLE IF NOT EXISTS `security_user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `password_hash` VARCHAR(256) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `student_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  INDEX `fk_security_user_student1_idx` (`student_id` ASC) VISIBLE,
  CONSTRAINT `fk_security_user_student1`
    FOREIGN KEY (`student_id`)
    REFERENCES `student` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- Table for Security Role

CREATE TABLE IF NOT EXISTS `security_role` (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_id_UNIQUE` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
);


-- Table for the Many-to-Many relationship between SecurityUser and SecurityRole

CREATE TABLE IF NOT EXISTS `user_has_role` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `fk_security_user_has_security_role_security_role1_idx` (`role_id` ASC) VISIBLE,
  INDEX `fk_security_user_has_security_role_security_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_security_user_has_security_role_security_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `security_user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_security_user_has_security_role_security_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `security_role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);