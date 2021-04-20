<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

//variable that are being represented in the select statement. 
$email = mysqli_real_escape_string($con,$_POST['email']);

//select statement that uses the users email to get their id and then grab their
//exercise goal based on that
$mysql_qry = "SELECT DISTINCT ug.GoalID
FROM User_Goal as ug, User as u
WHERE u.Email = '$email'
AND u.UserID = ug.UserID";

//displays the information
$result = mysqli_query($con, $mysql_qry) or die("Error in Selecting " . mysqli_error($con));

//this block of code changes the information in JSON format
$id = array();
while($row = mysqli_fetch_assoc($result))
{
	$id[] = $row;
}

echo json_encode($id);

mysqli_close($con);

?>