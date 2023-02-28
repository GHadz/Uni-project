<?php
    ob_start();
    session_start();
    require_once "config.php";
if(isset($_POST['username']) && isset($_POST['pass']))     {
        $stmt = mysqli_stmt_init($conn);
        $user_name = $_POST["username"];
        $password = $_POST["pass"];
        $hashed = md5($password);
        
        if ( mysqli_stmt_prepare($stmt,"SELECT userID FROM users WHERE userName=? AND userPassword=?")) {
            // Bind parameters
            mysqli_stmt_bind_param($stmt,"ss", $user_name,$hashed);
            // Execute query
            mysqli_stmt_execute($stmt);
            // Bind result variables
            mysqli_stmt_bind_result($stmt,$id);
            if(mysqli_stmt_fetch($stmt)){
                // Set session variable
                $_SESSION['userID'] = $id;
                header("location:../index.php");//echo the id
            }else {
              header("location: login.html");
            }
            // Close statement
            $stmt -> close();
        }
    }else{
        header("location: login.html");
    }
?>
