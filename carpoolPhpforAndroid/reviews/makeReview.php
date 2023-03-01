<?php 
 if(isset($_POST['userId'])&&isset($_POST['id'])&&isset($_POST['details'])&&isset($_POST['type'])&&isset($_POST['rating'])&&isset($_POST['rideId']))
 {
$reviewerId = $_POST['userId'];
$reviewedId = $_POST['reviewedId'];
$rideId = $_POST['rideId'];
$type = $_POST['type'];
$details = $_POST['details'];
$rating = $_POST['rating'];
require_once "conn.php";
$stmt = mysqli_stmt_init($conn);
$sql = "INSERT INTO reviews (reviewID,description,reviewType,rating,reviewedUserID,rideID,reviewerID) 
VALUES (NULL, ?, ?, ?, ?, ?, ?);";
if(mysqli_stmt_prepare($stmt,$sql))//insert query prepare
{
mysqli_stmt_bind_param($stmt,"ssssss",$details,$type,$rating,$id,$reviewedId,$rideId,$reviewerId); //bind variables
$stmt->execute();
  $result = mysqli_stmt_get_result($stmt);
  
  //check if the query was successful
  if(mysqli_stmt_affected_rows($stmt)>0){
      echo mysqli_stmt_insert_id($stmt);
  }else{
    echo "failure";
  }
  $stmt->close();
}

}

 
 ?>