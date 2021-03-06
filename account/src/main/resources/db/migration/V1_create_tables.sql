-- use database graphql
-- use schema user
DROP TABLE IF EXISTS t_teacher;
DROP TABLE IF EXISTS t_course;
DROP TABLE IF EXISTS t_score;
DROP TABLE IF EXISTS t_student;
CREATE TABLE IF NOT EXISTS t_student (id VARCHAR(36) PRIMARY KEY, c_name VARCHAR(20) NOT NULL, birthday DATE NOT NULL, gender VARCHAR(10) NOT NULL, create_time TIMESTAMP NOT NULL);
CREATE TABLE IF NOT EXISTS t_score (id VARCHAR(36) PRIMARY KEY, student_id VARCHAR(36) NOT NULL, course_id VARCHAR(36) NOT NULL, score DECIMAL(6, 2) NOT NULL, create_time TIMESTAMP NOT NULL);
CREATE TABLE IF NOT EXISTS t_course (id VARCHAR(36) PRIMARY KEY, c_name VARCHAR(20) NOT NULL, teacher_id VARCHAR(36) NOT NULL, create_time TIMESTAMP NOT NULL);
CREATE TABLE IF NOT EXISTS t_teacher (id VARCHAR(36) PRIMARY KEY, c_name VARCHAR(20) NOT NULL, create_time TIMESTAMP NOT NULL);
