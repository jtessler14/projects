<!DOCTYPE html>
<html>
<head>
 <title> select2A </title>
</head>
<body>
 
<?php
$con = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");

if(mysqli_connect_error()) {echo "Failed to connect to MySQL:" . mysqli_connect_error() . "<br>";}
else {echo "Established Database Connection. ";}

//$feature = mysqli_real_escape_string($con, $_POST['feature']);
$feature = mysqli_real_escape_string($con, $_POST['feature']);

echo "<p>" .$feature. "</p>";


$sql = "SELECT c.classroomNum as Room
FROM classroom AS c, classroom_features AS f
WHERE c.classroomNum = f.classroomNum AND f.features = '$feature'";

$result = mysqli_query($con, $sql);
$num_rows = mysqli_num_rows($result); //return the amount of rows returned

if ($num_rows > 0) {
 echo "<table border='1'>";
 echo "<tr><th>Room</th></tr>";
 while($row = mysqli_fetch_assoc($result)) {
  echo "<tr><td>".$row["Room"]."</td></tr>";
 }
echo "</table>"; }
else { echo "0 results";}
echo "<p align='center'><i>$num_rows Row(s)</i></p>";
mysqli_close($con);
?>

</body>
</html>
