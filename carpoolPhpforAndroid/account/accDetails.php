<?php 
if(isset($_POST["id"])){
    require_once "conn.php";
    $idS = $_POST["id"];
    $stmt = mysqli_stmt_init($conn);
    if ( mysqli_stmt_prepare($stmt,"SELECT users.userFirstName,users.userLastName,users.userName,users.userEmail,users.userPassword,users.userPhone,users.userDriverRating,users.userPassengerRating FROM users WHERE users.userID = ?")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"s", $idS);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        mysqli_stmt_bind_result($stmt,$fname,$lname,$username,$email,$pass,$phone,$driverRating,$passengerRating);
        $users = array();
        while(mysqli_stmt_fetch($stmt)){
            $tmp = array(); 
            $tmp['fname'] = $fname;
            $tmp['lname'] = $lname;
            $tmp['username'] = $username;
            $tmp['email'] = $email;
            $tmp['pass'] = $pass;
            $tmp['phone'] = $phone;
            $tmp['driverRating'] = $driverRating;
            $tmp['passengerRating'] = $passengerRating;
      
    
            array_push($users,$tmp);
        }
        // Close statement
        $stmt -> close();
      }
    
    
    echo json_encode($users);
    }

?>
