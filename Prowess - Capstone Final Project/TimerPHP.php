<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die('Connection failed : ('. $con->connect_errno .') '. $con->connect_error);
}

//variables that are being represented in the select statement. 
$timelength = mysqli_real_escape_string($con, $_POST['timelength']);
$userID = mysqli_real_escape_string($con, $_POST['userID']);

//insert statement which inserts the timer length and userID into the database
$mysql_qry = "INSERT INTO Timer (TimeLength, UserID) VALUES ('$timelength', '$userID')";
$result = mysqli_query($con, $mysql_qry);


$con->close();

?>

