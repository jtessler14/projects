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

//select statement selecting everything from a specific user based off their email
$mysql_qry = "SELECT * from User where Email = '$email'";
$result = mysqli_query($con, $mysql_qry);

//if the information exists, go directly to the home screen
//else, they go to confirm their information and their workout goal
if(mysqli_num_rows($result) > 0) {
echo "1";
}
else{
echo "0";
}

//$con->close();

?>