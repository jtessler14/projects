<!DOCTYPE html>
<html>
<head>
 <title> select1A </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) 
	{echo "Failed to connect to MySQL:" . mysqli_connect_error() ;}
else 
	{echo "Established Database Connection. ";}

$section = mysqli_real_escape_string($con, $_POST['section']); // what we pick from drop down */

echo "<p>" .$section. "</p>";

$sql = "SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as Student 
FROM students as st, section as s, student_section as ss 
WHERE s.sectionID = '$section' AND ss.studentID = st.id AND ss.sectionID = s.sectionID
ORDER BY st.last_name ASC, st.first_name ASC";

$result = mysqli_query($con, $sql); //imperative to work
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Student</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
    echo "<tr><td>".$row["Student"]."</td></tr>"; //grabs the data
 }
echo "</table>"; }
else {echo "0 results";} // only occurs if nothing is found
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>

