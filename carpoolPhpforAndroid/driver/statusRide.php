<?php
if(isset($_POST["id"])&&isset($_POST["status"])){
    //include the other needed files
    require_once "conn.php";

    $stmt = mysqli_stmt_init($conn);
    $id = $_POST["id"];
    $status = $_POST["status"];
   
    if ( mysqli_stmt_prepare($stmt,"UPDATE rides,statuses SET rides.statusID = statuses.statusID WHERE rides.rideID = ? AND statuses.statusName = ?;")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ss", $id,$status);
        // Execute query
         mysqli_stmt_execute($stmt);

         $result = mysqli_stmt_get_result($stmt);
        
           if (mysqli_stmt_affected_rows($stmt)>0)  //check if a row was edited
      {
          echo "success";
      }
    else echo "failure";

    $stmt -> close();

    }
    
}
?>