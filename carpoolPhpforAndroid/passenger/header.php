<?php 
 if(isset($_POST["id"])){
    require_once "conn.php";
    $idS = $_POST["id"];
    $stmt = mysqli_stmt_init($conn);
    //prepare query
    if ( mysqli_stmt_prepare($stmt,"SELECT users.userName,users.userEmail FROM users WHERE users.userID = ?;")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"s", $idS);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        mysqli_stmt_bind_result($stmt,$name,$email);
        $user = array();
        while(mysqli_stmt_fetch($stmt)){
            $tmp = array(); 
            $tmp['name'] = $name;
            $tmp['email'] = $email;
    
            array_push($user,$tmp);
        }
        // Close statement
        $stmt -> close();
      }
    
    
    echo json_encode($user);
}
 ?>
