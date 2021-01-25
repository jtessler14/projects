<!DOCTYPE html>
<html>
<head>
 <title> Select5A </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
if(mysqli_connect_error()) {echo "Failed to connect to MySQL:" . mysqli_connect_error() . "<br>";}
else {echo "Established Database Connection <br>";}

$studID = $_POST['courses2'];

echo "<p>" .$studID. "</p>";


$sql = "SELECT DISTINCT c.courseName, g.letterGPA, g.numGPA, c.creditHours
FROM course AS c, students AS s, student_section AS ss, grade AS g, section AS se
WHERE ss.studentID = s.id
AND ss.sectionID = se.sectionID
AND s.id = g.studentID
AND se.sectionID = g.sectionID
AND se.courseID = c.courseNum
AND s.id = '$studID'
ORDER BY se.startDate  ASC";

$result = mysqli_query($con, $sql);
if (mysqli_num_rows($result) > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Course</th><th>Grade</th><th>GPA</th><th>Credit Hours</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
  echo "<tr><td>".$row['courseName']."</td><td>".$row['letterGPA']."</td><td>".$row['numGPA']."</td><td>".$row['creditHours']."</td></tr>";
 }
echo "</table>"; }
else {
 echo "0 results";
}
mysqli_close($con);
?>
</body>
</html>
