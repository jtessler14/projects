<?php
require "conn.php";
$user_name = $_POST["user_name"];
$user_pass = $_POST["password"];
$mysql_qry = "select * from ITP5_UN_PW where Username like '$user_name' and Password like '$user_pass'";
$result = mysqli_query($con ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
echo "login success";
}
else{
echo "Login not successful. Username and/or Password maybe incorrect or the account may not exist";
}

?>