<?php
if(isset($_POST["id"])){
require_once "conn.php";
$idS = $_POST["id"];
$stmt = mysqli_stmt_init($conn);
if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,rides.startDate,rides.nbPassengers,rides.maxCapacity,statuses.statusName FROM users,statuses,rides WHERE rides.driverID = ? AND rides.statusID = statuses.statusID AND (statuses.statusName = 'Starting soon' OR statuses.statusName = 'Ongoing')  AND rides.driverID = users.userID ;")) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$date,$nbrPassengers,$Capacity,$status);
    $drives = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['date'] = $date;
        $tmp['nbrPass'] = $nbrPassengers;
        $tmp['Capacity'] = $Capacity;
        $tmp['status']=$status;
        array_push($drives,$tmp);
    }
    // Close statement
    $stmt -> close();
  }


echo json_encode($drives);
}
?>