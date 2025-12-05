CREATE TABLE IF NOT EXISTS students
(
ID INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
furigana VARCHAR(100) NOT NULL,
nickname VARCHAR(100),
email_address VARCHAR(100) NOT NULL,
area VARCHAR(100),
age INT,
gender VARCHAR(100),
remark TEXT,
is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
courses_ID INT AUTO_INCREMENT PRIMARY KEY ,
student_id INT NOT NULL,
course_name varchar(100) NOT NULL,
start_date TIMESTAMP,
scheduled_end_date TIMESTAMP
);