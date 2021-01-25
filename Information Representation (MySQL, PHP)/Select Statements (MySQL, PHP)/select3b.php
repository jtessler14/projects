<!DOCTYPE html>
<html>
<head>
 <title> Select3B </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) 
    {echo "Failed to connect to MySQL:" . mysqli_connect_error() ;}
else 
    {echo "Established Database Connection. ";}

$faculty = $_POST['faculty']; // what we pick from drop down */

echo "<p>" .$course. "</p>";

$sql = "SELECT DISTINCT f.facultyID, CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f
WHERE f.facultyID NOT IN (
SELECT DISTINCT f1.facultyID
FROM faculty as f1, section as s, course as c
WHERE f1.facultyID = s.facultyID
AND c.courseNum = '$faculty'
AND s.courseID = c.courseNum)";

$result = mysqli_query($con, $sql); //imperative to work
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Faculty</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
    echo "<tr><td>".$row["facultyName"]."</td></tr>"; //grabs the data
 }
echo "</table>"; }
else {echo "0 results";} // only occurs if nothing is found
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>

