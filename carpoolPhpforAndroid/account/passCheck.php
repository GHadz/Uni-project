<?php
if(isset($_POST["id"]) && isset($_POST["password"])){
    //include the other needed files
    require_once "conn.php";

    $stmt = mysqli_stmt_init($conn);
    $id = $_POST["id"];
    $password = $_POST["password"];
    $p = md5($password);
    
    if ( mysqli_stmt_prepare($stmt,"SELECT users.userID FROM users WHERE users.userID=? AND users.userPassword=?")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ss", $id,$p);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        $result = mysqli_stmt_get_result($stmt);
        
  
    if($result->num_rows > 0){

        echo "success";
    }else {
        echo "failure";
    }
      // Close statement
      $stmt -> close();
    }
}
?>