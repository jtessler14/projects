<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die('Connection failed : ('. $con->connect_errno .') '. $con->connect_error);
}


//variables that are being represented in the select statement. 
$fName = mysqli_real_escape_string($con, $_POST['fName']);
$lName = mysqli_real_escape_string($con,$_POST['lName']);
$email = mysqli_real_escape_string($con,$_POST['email']);
$DOB = mysqli_real_escape_string($con,$_POST['DOB']);
$height = mysqli_real_escape_string($con,$_POST['height']);
$weight = mysqli_real_escape_string($con,$_POST['weight']);

//Insert statement that inserts the user's info into the database when they access the application for the first time
$mysql_qry = "INSERT INTO User (fName, lName, Email, DOB, Height, Weight) VALUES ('$fName', '$lName', '$email', '$DOB', '$height', '$weight')";
$result = mysqli_query($con, $mysql_qry);


$con->close();

?>

