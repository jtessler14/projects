<!DOCTYPE html>
<html>
<head>
 <title> select4A </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
if(mysqli_connect_error())
{echo "Failed to connect to MySQL:" . mysqli_connect_error() . "<br>";}
else
{echo "Established Database Connection <br>";}

$courses = mysqli_real_escape_string($con, $_POST['courses']);
echo "<p>" .$courses. "</p>";

$sql = "SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as Student
FROM students as st, course as c, course_prerequisite as cp, student_section AS ss, section AS s
WHERE cp.courseNum = c.courseNum
AND st.id = ss.studentID
AND ss.sectionID = s.sectionID
AND c.courseNum = s.courseID
AND c.courseName = '$courses'";;

$result = mysqli_query($con, $sql);
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Name</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
  echo "<tr><td>".$row["Student"]."</td></tr>";
 }
echo "</table>"; }
else {
 echo "0 results";
}
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>
