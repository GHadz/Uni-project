<?php
if (isset($_POST["id"])) {

require_once "conn.php";
$idS =$_POST["id"];
$stmt = mysqli_stmt_init($conn);
$sql = "SELECT users.userID,users.userName,rides.rideID FROM users,rides,requestride,statuses A,statuses B WHERE requestride.passengerID = users.userID AND requestride.rideID = rides.rideID AND requestride.statusID = A.statusID AND A.statusName = 'Accepted' AND rides.statusID = B.statusId AND B.statusName = 'Ended' AND rides.driverID = ?";
if ( mysqli_stmt_prepare($stmt,$sql)) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$passengerName,$rideId);
    $requests = array();
    $type = "Passenger";
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['name'] = $passengerName;
        $tmp['rideId'] = $rideId;
        $tmp ['type'] = $type;
        array_push($requests,$tmp);
    }

  }
  $sql = "SELECT users.userID,users.userName,rides.rideID FROM users,rides,requestride,statuses A,statuses B WHERE requestride.passengerID = ? AND requestride.rideID = rides.rideID AND requestride.statusID = A.statusID AND A.statusName = 'Accepted' AND rides.statusID = B.statusId AND B.statusName = 'Ended' AND rides.driverID = users.userID";
  if ( mysqli_stmt_prepare($stmt,$sql)) {
      // Bind parameters
      mysqli_stmt_bind_param($stmt,"s", $idS);
      // Execute query
       mysqli_stmt_execute($stmt);
      // Bind result variables
      mysqli_stmt_bind_result($stmt,$id,$passengerName,$rideId);
        $type = "Driver";
      while(mysqli_stmt_fetch($stmt)){
          $tmp = array(); 
          $tmp['id'] = $id;
          $tmp['name'] = $passengerName;
          $tmp ['type'] = $type;
          $tmp['rideId'] = $rideId;
          array_push($requests,$tmp);
      }
      // Close statement
      $stmt -> close();
    }

echo json_encode($requests);
  }
?>