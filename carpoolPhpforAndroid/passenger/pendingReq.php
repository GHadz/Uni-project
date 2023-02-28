<?php
if(isset($_POST["id"])){
require_once "conn.php";
$idS = $_POST["id"];
$stmt = mysqli_stmt_init($conn);
if ( mysqli_stmt_prepare($stmt,"SELECT COUNT(requestride.requestID) FROM rides,requestride WHERE rides.rideID = ? AND requestride.rideID = rides.rideID AND requestride.statusID = 7;")) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$result);
        
         if (mysqli_stmt_fetch($stmt)) 
         {
             echo $result;
         }
       else echo "failure";
   
       $stmt -> close();

    // Close statement

  }


}
?>