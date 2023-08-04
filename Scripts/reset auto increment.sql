-- This might be useful
USE `acmecollege`;
ALTER TABLE student AUTO_INCREMENT = 1;
ALTER TABLE student_club AUTO_INCREMENT = 1;
ALTER TABLE club_membership AUTO_INCREMENT = 1;
ALTER TABLE membership_card AUTO_INCREMENT = 1;
ALTER TABLE professor AUTO_INCREMENT = 1;
ALTER TABLE course AUTO_INCREMENT = 1;
-- Note:  No auto_increment on course_registration table as it has a composite primary key