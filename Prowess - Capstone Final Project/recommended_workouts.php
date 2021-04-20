<?php

$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

$goalID = mysqli_real_escape_string($con,$_POST['goalID']);

if ($goalID === 1) {

echo "build muscle";

$mysql_qry = "SELECT ExerciseTitle
FROM Exercise
WHERE ExerciseID = 2
OR ExerciseID = 3
OR ExerciseID = 7
OR ExerciseID = 17
OR ExerciseID = 18
OR ExerciseID = 20
OR ExerciseID = 24
OR ExerciseID = 25
OR ExerciseID = 31
OR ExerciseID = 35
OR ExerciseID = 38
OR ExerciseID = 42
OR ExerciseID = 50
OR ExerciseID = 58
OR ExerciseID = 60
OR ExerciseID = 64
OR ExerciseID = 73
OR ExerciseID = 65";

$result = mysqli_query($con, $mysql_qry) or die("Error in Selecting " . mysqli_error($con));

$exerciseTitle = array();
while($row = mysqli_fetch_assoc($result2))
{
	$exerciseTitle[] = $row;
}

echo json_encode($exerciseTitle);

mysqli_close($con);

}


elseif($goalID === 2) {

	echo "lose fat";
	
$mysql_qry2 = "SELECT ExerciseTitle
FROM Exercise
WHERE ExerciseID = 4
OR ExerciseID = 6
OR ExerciseID = 11
OR ExerciseID = 19
OR ExerciseID = 23
OR ExerciseID = 20
OR ExerciseID = 34
OR ExerciseID = 24
OR ExerciseID = 32
OR ExerciseID = 48
OR ExerciseID = 49
OR ExerciseID = 46
OR ExerciseID = 62
OR ExerciseID = 61
OR ExerciseID = 52
OR ExerciseID = 74
OR ExerciseID = 63
OR ExerciseID = 71";

$result2 = mysqli_query($con, $mysql_qry2) or die("Error in Selecting " . mysqli_error($con));

$exerciseTitle = array();
while($row = mysqli_fetch_assoc($result2))
{
	$exerciseTitle[] = $row;
}

echo json_encode($exerciseTitle);

mysqli_close($con);

}


elseif($goalID === 3) {

	echo "flexibility";
	
$mysql_qry3 = "SELECT StretchTitle
FROM Stretch
WHERE StretchID = 1
OR StretchID = 2
OR StretchID = 3
OR StretchID = 4
OR StretchID = 5
OR StretchID = 6
OR StretchID = 7
OR StretchID = 8
OR StretchID = 9
OR StretchID = 10
OR StretchID = 11
OR StretchID = 12 
OR StretchID = 13
OR StretchID = 14
OR StretchID = 15
OR StretchID = 16
OR StretchID = 17
OR StretchID = 18
OR StretchID = 19
OR StretchID = 20
OR StretchID = 21
OR StretchID = 22";

$result3 = mysqli_query($con, $mysql_qry3) or die("Error in Selecting " . mysqli_error($con));

$exerciseTitle = array();
while($row = mysqli_fetch_assoc($result3))
{
	$exerciseTitle[] = $row;
}

echo json_encode($exerciseTitle);

mysqli_close($con);

}





?>


