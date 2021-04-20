<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die('Connection failed : ('. $con->connect_errno .') '. $con->connect_error);
}


//variable that are being represented in the select statement. 
$time = mysqli_real_escape_string($con, $_POST['time']);
$userID = mysqli_real_escape_string($con, $_POST['userID']);

//select statement that inserts the stopwatch time and the userID together into the Stopwatch Table
$mysql_qry = "INSERT INTO StopWatch (Time, UserID) VALUES ('$time', '$userID')";
$result = mysqli_query($con, $mysql_qry);


$con->close();

?>