INSERT INTO students(name, furigana, nickname, email_address, area, age, gender,is_deleted)
VALUES('John Miller', 'Jon Mira', 'Johnny', 'john.miller@example.com', 'New York', 20, 'male', false),
('Emily Johnson', 'Emiri Jonson', 'Em', 'emily.johnson@example.com', 'Los Angeles', 19, 'female', false),
('Michael Brown', 'Maikeru Buraun', 'Mike', 'michael.brown@example.com', 'Chicago', 22, 'male', false),
('Sophia Davis', 'Sofia Deibisu', 'Sophie', 'sophia.davis@example.com', 'Houston', 18, 'female', false),
('Alex Smith', 'Arekkusu Sumisu', 'Alex', 'alex.smith@example.com', 'Seattle', 21, 'other', false);

INSERT INTO students_courses(student_id, course_name, start_date, scheduled_end_date)
VALUES(1, 'Java Programming Basics', '2025-04-01 09:00:00', '2025-07-01 09:00:00'),
(2, 'Web Development with HTML', '2025-04-05 10:00:00', '2025-07-05 10:00:00'),
(3, 'Database Fundamentals', '2025-04-10 13:00:00', '2025-07-10 13:00:00'),
(4, 'Python for Beginners', '2025-04-15 14:00:00', '2025-07-15 14:00:00'),
(5, 'Fullstack Development', '2025-04-20 15:00:00', '2025-07-20 15:00:00');

INSERT INTO course_status(courses_id, status)
VALUES(1, 'TEMPORARY'),
(2, 'APPLIED'),
(3, 'IN_PROGRESS'),
(4, 'COMPLETED'),
(5, 'TEMPORARY');
