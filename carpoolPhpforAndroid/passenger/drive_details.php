<?php
if(isset($_POST["id"])){
require_once "conn.php";
$idS = $_POST["id"];
$stmt = mysqli_stmt_init($conn);
if ( mysqli_stmt_prepare($stmt,"SELECT users.userName,users.userDriverRating,rides.startDate,rides.details,rides.maxCapacity,rides.nbPassengers,users.userPhone,A.locationName,B.locationName,COUNT(requestride.rideId) FROM rides,users,locations A,locations B,requestride WHERE rides.rideID = ? AND requestride.statusID=5 AND rides.driverID = users.userID AND rides.sourceID = A.locationID AND rides.destinationID = B.locationID;")) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$name,$rating,$start,$details,$capacity,$nbrpass,$phone,$source,$destination,$countAccepted);
    $users = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['name'] = $name;
        $tmp['date'] = $start;
        $tmp['details'] = $details;
        $tmp['seats'] = $capacity - $nbrpass;
        $tmp['phone'] = $phone;
        $tmp['source'] = $source;
        $tmp['destination'] = $destination;
        $tmp['rating'] = $rating;
        $tmp['countAccepted'] = $countAccepted;
  

        array_push($users,$tmp);
    }
    // Close statement
    $stmt -> close();
  }


echo json_encode($users);
}
?>