<?php
if(isset($_POST["id"])){
require_once "conn.php";
$idS = $_POST["id"];
$stmt = mysqli_stmt_init($conn);
if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,statuses,requestride,locations A,locations B WHERE requestride.passengerID = ?  AND requestride.rideID = rides.rideID AND requestride.statusID = statuses.statusID AND statuses.statusName = 'Accepted' AND rides.driverID = users.userID AND rides.sourceID = A.locationID  AND rides.destinationID = b.locationID AND (rides.statusId ='1' OR rides.statusId = '2');")) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$name,$date,$source,$destination);
    $requests = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['name'] = $name;
        $tmp['date'] = $date;
        $tmp['src'] = $source;
        $tmp['dest'] = $destination;

        array_push($requests,$tmp);
    }
    // Close statement
    $stmt -> close();
  }


echo json_encode($requests);
}
?>