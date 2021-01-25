<!DOCTYPE html>
<html>
<head>
 <title> madeUp2 </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) 
    {echo "Failed to connect to MySQL:" . mysqli_connect_error() ;}
else 
    {echo "Established Database Connection. ";}


$sql = "SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as Student_Names, g.letterGPA
FROM section as s, students as st, student_section as ss, grade AS g
WHERE s.semester = 'spring'
AND (letterGPA = 'A-'
OR letterGPA = 'A'
OR letterGPA = 'A+')
AND st.id = ss.studentID
AND ss.sectionID = s.sectionID";

$result = mysqli_query($con, $sql); //imperative to work
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Names</th><th>GPA</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
    echo "<tr><td>".$row["Student_Names"]."</td><td>".$row["letterGPA"]."</td></tr>"; //grabs the data
 }
echo "</table>"; }
else {echo "0 results";} // only occurs if nothing is found
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>
