<!doctype html>
<html>
<body>
<h2>I308 Final Project</h2>
<h3>1A: Produce a roster for *specified section* sorted by students last name, first name </h3>
<form action="select1A.php" method="POST">

Section: <select name="section">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT s.sectionID, c.courseName
FROM section as s, course as c
WHERE c.courseNum = s.courseID
ORDER BY sectionID ASC;");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($sectionID,$sectionID);
  $sectionID = $row['sectionID'];
  $sectionID = $row['sectionID'];
  echo '<option value="'.$sectionID.'">'.$sectionID.'</option>';
 }
 
 
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Roster">
</form>

<br>

<h3> 2A: Produce a list of rooms that are equipped with *some feature* </h3>
<form action="select2A.php" method="POST">

Features: <select name="feature">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT features FROM classroom_features");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($features);
  $features = $row['features'];
  echo '<option value="'.$features.'">'.$features.'</option>';
 }
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Room List">
</form>

<br> 


<h3> 4A: Produce a list of students who are eligible to register for a *specified course* that has a prerequisite. </h3>
<form action="select4A.php" method="POST">

Courses: <select name="courses">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT courseName FROM course");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($courseName);
  $courseName = $row['courseName'];
  echo '<option value="'.$courseName.'">'.$courseName.'</option>';
 }
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Student List">
</form>

<br>

<h3> 5A: Provide a chronological list (transcript-like) of all courses taken by a *specificied* student. Show grades earned.
 </h3>
<form action="select5A.php" method="POST">

Students: <select name="courses2">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT id, CONCAT(first_name, ' ', last_name) as fullname FROM students");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($id, $fullname);
  $id = $row['id'];
  $fullname = $row['fullname'];
  echo '<option value="'.$id.'">'.$fullname.'</option>';
 }
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Course List">
</form>


<br>

<h3> 7A: Produce an alphabetical list of students with their majors who are advised by a *specified advisor* </h3>
<form action="select7A.php" method="POST">
Advisors: <select name="advisor">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT name, advisorID from advisor");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($advisorID, $name);
  $advisorID = $row['advisorID'];
  $name = $row['name'];
  echo '<option value="'.$advisorID.'">'.$name.'</option>';
 }
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Student List">
</form>

<h3> 3B: Produce a list of faculty who have never taught a *specified course* </h3>
<form action="select3b.php" method="POST">
Course: <select name="faculty">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT DISTINCT courseNum, courseName from course");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($courseNum, $courseName);
  $courseNum = $row['courseNum'];
  $courseName = $row['courseName'];
  echo '<option value="'.$courseNum.'">'.$courseName.'</option>';
 }
?>
 </select>
<br><br>
<input type="submit" name="submit" value="Produce Faculty List">
</form>



<h3> 6C: Produce a list of students and faculty who were in a *particular building* at a *particular time*.
 Also include in the list faculty and advisors who have offices in that building. </h3>
 
<form action="select6c.php" method="POST">

Building Name: <select name="buildingName">
<?php
$conn = mysqli_connect("db.sice.indiana.edu", "i308f19_team02", "my+sql=i308f19_team02", "i308f19_team02");
// Check connection
if (!$conn) {
 die("Connection failed: " . mysqli_connect_error() . "<br>");
}
 $result = mysqli_query($conn, "SELECT buildingId, buildingName from building");

 while ($row = mysqli_fetch_assoc($result)) {
  unset($buildingId, $buildingName);
  $buildingId = $row['buildingId'];
  $buildingName = $row['buildingName'];
  echo '<option value="'.$buildingName.'">'.$buildingName.'</option>';
 }
?>
 </select>
<br><br>
 
Time: <input type="time" name="time" maxlength=50 size=50 required>

<br><br>
<input type="submit" name="submit" value="Produce List">
</form>

<br><br>
<h3> Custom Query 1: Return all chairpeople's emails </h3>
<form action="madeUp1.php" method="POST">
<input type="submit" name="submit" value="Return Chairpeople">
</form>

<br><br>
<h3> Custom Query 2: Return all students in a spring semester class who received a grade of an A- or higher</h3>
<form action="madeUp2.php" method="POST">
<input type="submit" name="submit" value="Produce Student List">
</form>
</body>
</html>
