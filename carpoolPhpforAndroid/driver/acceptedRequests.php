<?php
if(isset($_POST["id"])){
require_once "conn.php";
$idS = $_POST["id"];
$stmt = mysqli_stmt_init($conn);
if ( mysqli_stmt_prepare($stmt,"SELECT requestride.requestID,requestride.requestDetails,users.userName,users.userPassengerRating,users.userPhone FROM users,requestride,statuses WHERE users.userID = requestride.passengerID AND requestride.statusID = statuses.statusID AND statuses.statusName = 'Accepted' AND requestride.rideID = ?;")) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$det,$passengerName,$rating,$phone);
    $drives = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['details'] = $det;
        $tmp['name'] = $passengerName;
        $tmp['phone']=$phone;
        $tmp['rating']=$rating;

        array_push($drives,$tmp);
    }
    // Close statement
    $stmt -> close();
  }


echo json_encode($drives);
}
?>