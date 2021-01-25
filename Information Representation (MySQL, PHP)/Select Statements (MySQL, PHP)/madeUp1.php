<!DOCTYPE html>
<html>
<head>
 <title> madeUp1 </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) 
    {echo "Failed to connect to MySQL:" . mysqli_connect_error() ;}
else 
    {echo "Established Database Connection. ";}



$sql = "SELECT DISTINCT CONCAT(f.firstName, ' ', f.lastName) as chairperson, fe.email
FROM faculty as f, department as d, faculty_email AS fe
WHERE d.chairperson = f.facultyID
AND f.facultyID = fe.facultyID";

$result = mysqli_query($con, $sql); //imperative to work
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Chairperon</th><th>Email</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
    echo "<tr><td>".$row["chairperson"]."</td><td>".$row["email"]."</td></tr>"; //grabs the data
 }
echo "</table>"; }
else {echo "0 results";} // only occurs if nothing is found
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>
</body>
</html>

