<?php

$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

//select statement that brings back certain fields of the table 
$mysql_qry = "SELECT BodyPart, ExerciseTitle from Exercise";

//displays the results
$result = mysqli_query($con, $mysql_qry) or die("Error in Selecting " . mysqli_error($con));


//the follow block of code converts the mysql information into a JSON format
$user = array();
while($row = mysqli_fetch_assoc($result))
{
	$Exercise[] = $row;
}

echo json_encode(['Exercise' => $Exercise]);

mysqli_close($con);

?>