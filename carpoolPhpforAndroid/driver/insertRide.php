<?php 
 if(isset($_POST['userId'])&&isset($_POST['date'])&&isset($_POST['details'])&&isset($_POST['max'])&&isset($_POST['src'])&&isset($_POST['dest']))
 {
$id = $_POST['userId'];
$date = $_POST['date'];
$details = $_POST['details'];
$max = $_POST['max'];
$src = $_POST['src'];
$dest = $_POST['dest'];
require_once "conn.php";
$stmt = mysqli_stmt_init($conn);
$sql = "INSERT INTO rides (rides.startDate,rides.maxCapacity,rides.details,rides.nbPassengers,rides.driverID,rides.sourceID,rides.destinationID,rides.statusID)
VALUES (?,?,?,0,?,?,?,(SELECT statuses.statusID FROM statuses WHERE statuses.statusName = 'Starting soon'))";
if(mysqli_stmt_prepare($stmt,$sql))//insert query prepare
{
mysqli_stmt_bind_param($stmt,"ssssss",$date,$max,$details,$id,$src,$dest); //bind variables
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
