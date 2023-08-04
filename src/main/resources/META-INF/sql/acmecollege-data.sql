USE `acmecollege`;

-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)

-- data for table `professor`
INSERT INTO `professor` (`professor_id`, `first_name`, `last_name`, `department`, `created`, `updated`) 
  VALUES (1,'Teddy','Yap','Information and Communications Technology',now(), now());

-- data for table `student`
INSERT INTO `student` (`id`, `first_name`, `last_name`,  `created`, `updated`)
  VALUES (1,'John','Smith',now(),now());

-- data for table `course`
INSERT INTO `course` (`course_id`, `course_code`, `course_title`, `year`, `semester`, `credit_units`, `online`, `created`, `updated`)
  VALUES (1,'CST8277','Enterprise Application Programming',2022,'AUTUMN',3,0,now(),now()),(2,'CST8284','Object-Oriented Programming in Java',2023,'WINTER',3,1,now(),now());

-- data for table `student_club`
INSERT INTO `student_club` (`club_id`, `name`, `academic`,`created`, `updated`)
  VALUES (1,'Computer Programming Club',1,now(),now()),(2,'Mountain Hiking Club',0,now(),now());

-- data for table `club_membership`
INSERT INTO `club_membership` (`membership_id`, `club_id`, `start_date`, `end_date`, `active`, `created`, `updated`)
  VALUES (1,2,now(),now(),0,now(),now()),(2,2,now(),now(),0,now(),now());

-- data for table `course_registration`
INSERT INTO `course_registration` (`student_id`, `course_id`, `numeric_grade`, `letter_grade`, `professor_id`, `created`, `updated`)
  VALUES (1,1,100,'A+',1,now(),now()),(1,2,85,NULL,NULL,now(),now());

-- data for table `membership_card`
INSERT INTO `membership_card` (`card_id`, `student_id`, `membership_id`, `signed`, `created`, `updated`)
  VALUES (1,1,1,1,now(),now()),(2,1,2,0,now(),now());

-- data for table `security_role`
INSERT INTO `security_role` (`role_id`, `name`)
  VALUES (1,'ADMIN_ROLE'), (2,'USER_ROLE');

-- data for table `security_user`
-- value for `password_hash` column computed by PBKDF2HashGenerator
--   user 'admin', password 'admin'
--   user 'cst8277', password '8277'
INSERT INTO `security_user` (`user_id`, `password_hash`, `username`, `student_id`)
  VALUES (1, 'PBKDF2WithHmacSHA256:2048:hYKwYbuwalL2mbXT3Lx8QgJuTWT8GgZcGljMPEW+TZA=:6GmiBW47QsKVgqF7wzt/wjQAMDd0RVMok3M8WPu8Y1U=', 'admin', null), (2, 'PBKDF2WithHmacSHA256:2048:ZJC4ipE7LQOZzOQyd2ch7VOxHJWwrVfDFTbo9H+U5Fw=:j5Wulo/tVmolv8hqu0k5ejTOPEMbzviQXStg/0/c6Qo=', 'cst8277', 1);

--  data for table `user_has_role`
INSERT INTO `user_has_role` (`user_id`, `role_id`)
  VALUES (1,1), (2,2);
