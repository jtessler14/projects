<!DOCTYPE html>
<html>
<head>
 <title> Select7A </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
if(mysqli_connect_error()) {echo "Failed to connect to MySQL:" . mysqli_connect_error() . "<br>";}
else {echo "Established Database Connection <br>";}

$advisor = $_POST['advisor'];




$sql = "SELECT CONCAT(st.first_name, ' ', st.last_name) as fullname, m.major as major
FROM students as st, student_advisor as sa, student_majors as sm, major as m
WHERE sa.studentID=st.id
AND sa.advisorID='$advisor'
AND st.id=sm.studentID
AND m.majorID=sm.majorID
ORDER BY fullname ASC";


$result = mysqli_query($con, $sql);
if (mysqli_num_rows($result) > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Student</th><th>Major</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
  echo "<tr><td>".$row['fullname']."</td><td>".$row['major']."</td></td></tr>";
 }
echo "</table>"; }
else {
 echo "0 results";
}
mysqli_close($con);
?>
</body>
</html>
