/*Team 02 
Kelly Janis, Scott Tangney, Davis Joseph, Jayson Tessler

/*1a. Produce a roster for *specified section* sorted by students last name, first name*/

SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as Student
FROM students as st, section as s, student_section as ss
WHERE s.sectionID = st.id AND s.sectionID= ss.studentID
ORDER BY st.last_name ASC, st.first_name ASC;

/*2a. Produce a list of rooms that are equipped with *some feature* —e.g., “wired instructor station”. */

SELECT c.classroomNum 
FROM classroom AS c, classroom_features AS f
WHERE c.classroomNum = f.classroomNum;

/*3b.  Produce a list of faculty who have never taught a *specified course**/

SELECT DISTINCT f.facultyID, CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f
WHERE f.facultyID NOT IN (
SELECT DISTINCT f1.facultyID
FROM faculty as f1, section as s, course as c
WHERE f1.facultyID = s.facultyID
AND c.courseName = 'Social Informatics'
AND s.courseID = c.courseNum);

/*4A: Produce a list of students who are eligible to register for a *specified course* that has a prerequisite.*/

SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name)
FROM students as st, course as c, course_prerequisite as cp, student_section AS ss, section AS s
WHERE cp.courseNum = c.courseNum
AND st.id = ss.studentID
AND ss.sectionID = s.sectionID
AND c.courseNum = s.courseID;

/*5A. Provide a chronological list (transcript-like) of all courses taken by a *specificied* student. Show grades earned.*/

SELECT DISTINCT c.courseName, g.letterGPA, g.numGPA, c.creditHours
FROM course AS c, students AS s, student_section AS ss, grade AS g, section AS se
WHERE ss.studentID = s.id
AND ss.sectionID = se.sectionID
AND s.id = g.studentID
AND se.sectionID = g.sectionID
AND se.courseID = c.courseNum
AND CONCAT(s.first_name, ' ',s.last_name) = 'Sheba Janowski'
ORDER BY se.startDate  ASC;

/*7a. Produce an alphabetical list of students with their majors who are advised by a*specified advisor*/

SELECT CONCAT(st.first_name, ' ', st.last_name) as fullname, m.major
FROM students as st, advisor as a, major as m
WHERE st.id = a.advisorID
AND a.majorID = m.majorID
ORDER BY fullname ASC;

/*6C: Produce a list of students and faculty who were in a *particular building* at a *particular time*. Also include in the list faculty and advisors who have offices in that building.*/

SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as All_Names
FROM students as st, section as s, student_section as ss, faculty AS f
WHERE 
st.id = ss.studentID AND 
s.sectionID = ss.sectionID
AND '123000' BETWEEN s.startTime AND s.endTime
UNION
SELECT CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f, section as s
WHERE f.facultyID = s.facultyID
AND '123000' BETWEEN s.startTime AND s.endTime
UNION
SELECT a.name
FROM advisor AS a, building AS b, office AS o
WHERE a.officeID = o.officeID
AND b.buildingID = o.buildingID
AND b.buildingName = 'Scott Hall'
UNION
SELECT CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f, building AS b, office AS o
WHERE f.officeID = o.officeID
AND b.buildingID = o.buildingID
AND b.buildingName = 'Scott Hall';

/*Made up Queries*/

/*1. Return all chairpeople from department and their faculty email */
SELECT DISTINCT CONCAT(f.firstName, ' ', f.lastName), fe.email
FROM faculty as f, department as d, faculty_email AS fe
WHERE d.chairperson = f.facultyID
AND f.facultyID = fe.facultyID;



/*2. 2. return all students and their grades in spring semester classes if their letter GPA is an A- or higher that semester.*/

SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as Student_Names, g.letterGPA
FROM section as s, students as st, student_section as ss, grade AS g
WHERE s.semester = 'spring'
AND (letterGPA = 'A-'
OR letterGPA = 'A'
OR letterGPA = 'A+')
AND st.id = ss.studentID
AND ss.sectionID = s.sectionID;



