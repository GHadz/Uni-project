<?php
if(isset($_POST["id"])&&isset($_POST["userId"])&&isset($_POST["details"])&&isset($_POST["nbr"])){
    require_once "conn.php";
    $idS = $_POST["id"];
    $det = $_POST["details"];
    $uId = $_POST["userId"];
    $nbr = $_POST["nbr"]; 
    $stmt = mysqli_stmt_init($conn);
    if ( mysqli_stmt_prepare($stmt,"INSERT INTO requestride VALUES (NULL,?,?,?,?,'7');")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ssss", $det,$nbr,$uId,$idS);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        $result = mysqli_stmt_get_result($stmt);
        // Close statement
        
      }
      if (mysqli_stmt_affected_rows($stmt)>0) 
      {
          echo "success";
      }
    else echo "failure";

    $stmt -> close();

}


?>