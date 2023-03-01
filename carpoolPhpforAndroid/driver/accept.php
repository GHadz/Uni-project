<?php
if(isset($_POST["id"])){
    //include the other needed files
    require_once "conn.php";

    $stmt = mysqli_stmt_init($conn);
    $id = $_POST["id"];

    
    if ( mysqli_stmt_prepare($stmt,"UPDATE requestride,statuses SET requestride.statusID = statuses.statusID WHERE requestride.requestID = ? AND statuses.statusName = 'Accepted';")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"s", $id);
        // Execute query
         mysqli_stmt_execute($stmt);

         $result = mysqli_stmt_get_result($stmt);
        
           if (mysqli_stmt_affected_rows($stmt)>0)  //check if a row was deleted
      {
          echo "success";
      }
    else echo "failure";

    $stmt -> close();

    }
    
}
?>