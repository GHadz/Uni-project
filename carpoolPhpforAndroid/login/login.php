<?php
if(isset($_POST["email"]) && isset($_POST["password"])){
    //include the other needed files
    require_once "conn.php";

    $stmt = mysqli_stmt_init($conn);
    $email = $_POST["email"];
    $password = $_POST["password"];
    $p = md5($password);
    
    if ( mysqli_stmt_prepare($stmt,"SELECT userID FROM users WHERE userEmail=? AND userPassword=?")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ss", $email,$p);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        mysqli_stmt_bind_result($stmt,$id);
        
  
    if(mysqli_stmt_fetch($stmt)){

        echo($id); //echo the id
    }else {
        echo "failure";
    }
      // Close statement
      $stmt -> close();
    }
}
?>