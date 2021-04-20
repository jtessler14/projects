<?php
$con = mysqli_connect("db.luddy.indiana.edu", "i494f20_team54", "my+sql=i494f20_team54", "i494f20_team54");

//if statements that shows if there is a proper connection to the database
if ($con->connect_error) {
    die('Connection failed : ('. $con->connect_errno .') '. $con->connect_error);
}

//variables that are being represented in the select statement. 
$userID = mysqli_real_escape_string($con, $_POST['userID']);
$date = mysqli_real_escape_string($con, $_POST['date']);
$exerciseTitle = mysqli_real_escape_string($con, $_POST['exerciseTitle']);
$weight = mysqli_real_escape_string($con, $_POST['weight']);
$reps = mysqli_real_escape_string($con, $_POST['reps']);
$sets = mysqli_real_escape_string($con, $_POST['sets']);
$max = mysqli_real_escape_string($con, $_POST['max']);


//insert statement that inserts the userID and the date into the WorkoutPlan table
$mysql_qry = "INSERT INTO WorkoutPlan (UserID, Date) VALUES ('$userID', '$date')";
$result = $con->query($mysql_qry);

//the values of the table are inserted, it will run the if statement
if ($mysql_qry){
	//represents the last PlanID put into the WorkoutPlan table
	$last_id = mysqli_insert_id($con);
	echo "New record created successfully. Last inserted ID is: " . $last_id;
}
//inserts the following criteria into the Exercise_WorkoutPlan table... The $last_id represents the PlanID which was last put into the WorkoutPlan table
$mysql_qry2 = "INSERT INTO Exercise_WorkoutPlan (PlanID, ExerciseTitle, Weight, Reps, Sets, MaxWeight, UserID) VALUES ('$last_id', '$exerciseTitle', '$weight', '$reps', '$sets', '$max', '$userID')";
$result = $con->query($mysql_qry2);


$con->close();
?>



