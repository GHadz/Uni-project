<?php
if(isset($_POST["email"]) && isset($_POST["password"])){
    //include the other needed files
    require_once "conn.php";
    require_once "validate.php";
    //validate function to store the data properly
    $stmt = mysqli_stmt_init($conn);
    $email = validate($_POST["email"]);
    $password = validate($_POST["password"]);
    $p = md5($password);
    
    if ( mysqli_stmt_prepare($stmt,"SELECT * FROM users WHERE email=? AND password=?")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"ss", $email,$p);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        $result = mysqli_stmt_get_result($stmt);
        // Close statement
        $stmt -> close();
      }
    if($result->num_rows > 0){
        echo "success";
    }else {
        echo "failure";
    }

}

?>