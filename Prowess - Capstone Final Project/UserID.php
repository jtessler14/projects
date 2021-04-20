<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

//variable that are being represented in the select statement. 
$email = mysqli_real_escape_string($con,$_POST['email']);

//select statement that grabs the userID from the User table based on the email in android studio
$mysql_qry = "SELECT UserID FROM User WHERE Email = '$email';";

//displays the results
$result = mysqli_query($con, $mysql_qry) or die("Error in Selecting " . mysqli_error($con));


//the follow block of code converts the mysql information into a JSON format
$id = array();
while($row = mysqli_fetch_assoc($result))
{
	$id[] = $row;
}

echo json_encode($id);

mysqli_close($con);

?>