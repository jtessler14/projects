<!DOCTYPE html>
<html>
<head>
 <title> select6C </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) 
    {echo "Failed to connect to MySQL:" . mysqli_connect_error() ;}
else 
    {echo "Established Database Connection. ";}

$buildingName = $_POST['buildingName'];
$time = $_POST['time'];


$sql = "SELECT DISTINCT CONCAT(st.first_name, ' ', st.last_name) as All_Names
FROM students as st, section as s, student_section as ss, faculty AS f
WHERE
st.id = ss.studentID AND
s.sectionID = ss.sectionID
AND '$time' BETWEEN s.startTime AND s.endTime
UNION
SELECT CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f, section as s
WHERE f.facultyID = s.facultyID
AND '$time' BETWEEN s.startTime AND s.endTime
UNION
SELECT a.name
FROM advisor AS a, building AS b, office AS o
WHERE a.officeID = o.officeID
AND b.buildingID = o.buildingID
AND b.buildingName = '$buildingName'
UNION
SELECT CONCAT(f.firstName, ' ', f.lastName) as facultyName
FROM faculty AS f, building AS b, office AS o
WHERE f.officeID = o.officeID
AND b.buildingID = o.buildingID
AND b.buildingName = '$buildingName'";




$result = mysqli_query($con, $sql); //imperative to work
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>All_Names</th></tr>";
 while($row = mysqli_fetch_assoc($result)) { 

echo "<tr><td>".$row['All_Names']."</td></tr>";
 }
echo "</table>"; }
else {echo "0 results";} // only occurs if nothing is found
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>
