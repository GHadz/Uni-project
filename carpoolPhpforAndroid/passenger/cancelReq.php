<?php
if(isset($_POST["id"]) && isset($_POST["userId"])){
    //include the other needed files
    require_once "conn.php";
    $stmt = mysqli_stmt_init($conn);
    $id = $_POST["id"];
    $userId = $_POST["userId"];
    //prepare query
    if ( mysqli_stmt_prepare($stmt,"UPDATE requestride,statuses A,statuses B SET requestride.statusID = B.statusID WHERE requestride.rideID = ? AND requestride.passengerID = ? AND requestride.statusID = A.statusID AND (A.statusName = 'Accepted' OR A.statusName ='Pending')  AND B.statusName = 'Cancelled';")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ss", $id,$userId);
        // Execute query
         mysqli_stmt_execute($stmt);

         $result = mysqli_stmt_get_result($stmt);
        
       
             echo "success";
  
   
       $stmt -> close();

    }
    
}
?>
