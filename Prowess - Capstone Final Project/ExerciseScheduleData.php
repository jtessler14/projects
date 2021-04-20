<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

//variables that are being represented in the select statement. 
$email = mysqli_real_escape_string($con,$_POST['email']);
$date = mysqli_real_escape_string($con,$_POST['date']);

//select statement that selects the following information based off of a certain set of critera
$mysql_qry = "SELECT DISTINCT e.ExerciseTitle, ewp.Weight, ewp.Reps, ewp.Sets, ewp.MaxWeight
FROM Exercise AS e,Exercise_WorkoutPlan AS ewp, WorkoutPlan AS w, User as u
WHERE u.Email = '$email'
AND u.UserID = ewp.UserID AND u.UserID = w.UserID
AND ewp.ExerciseTitle=e.ExerciseTitle
AND ewp.PlanID=w.PlanID
AND w.Date = '$date'";

//displays the results
$result = mysqli_query($con, $mysql_qry) or die("Error in Selecting " . mysqli_error($con));

//the follow block of code converts the mysql information into a JSON format
$user = array();
while($row = mysqli_fetch_assoc($result))
{
	$user[] = $row;
}

echo json_encode($user);

mysqli_close($con);

?>




