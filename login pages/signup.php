<?php
// start the session
session_start();

if(isset($_POST['username'])&&isset($_POST['email'])&&isset($_POST['pass']))
{
    // include config and function files
    require_once "config.php";
    require_once "domain_exists.php";

    // get the inputed datas
    $name = $_POST['username'];
    $fName = $_POST['firstName'];
    $lName = $_POST['lastName'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $password = $_POST['pass'];
    $retypePassword = $_POST['repass'];
    $p = md5($password);

    // basic email format check
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $_SESSION['error'] = "Invalid email format";
        header("location: signup.html");
        exit;
    }
    elseif(!domain_exists($email)) // domain check
    {
        $_SESSION['error'] = "Invalid email domain";
        header("location: signup.html");
        exit;
    }
    else // check if email was already created
    {
        $stmt = mysqli_stmt_init($conn);
        if (mysqli_stmt_prepare($stmt,"SELECT * FROM users WHERE userEmail=?")) { // sql injection protection
            // Bind parameters
            mysqli_stmt_bind_param($stmt,"s", $email);
            // Execute query
            mysqli_stmt_execute($stmt);
            // Bind result variables
            $result = mysqli_stmt_get_result($stmt);
            // Close statement
            mysqli_stmt_close($stmt);
        }

        if ($result->num_rows > 0) { // email already registered
            $_SESSION['error'] = "Email already registered";
            header("location: signup.html");
            exit;
        }
        else { // email not registered
            $stmt2 = mysqli_stmt_init($conn); // init
            if(mysqli_stmt_prepare($stmt2,"INSERT INTO users (userFirstName,userLastName,userName,userEmail,userPassword,userPhone) 
            VALUES (?,?,?,?,?,?)")) { // insert query prepare
                mysqli_stmt_bind_param($stmt2,"ssssss",$fName,$lName,$name, $email,$p,$phone); // bind variables
                mysqli_stmt_execute($stmt2);
                // check if the query was successful
                if(mysqli_stmt_affected_rows($stmt2) > 0){
                    $_SESSION['success'] = "User created successfully";
                    header("location: login.html"); 
                } else {
                    $_SESSION['error'] = "User creation failed";
                    header("location: signup.html"); 
                }
                mysqli_stmt_close($stmt2);
            }
        }
    }
}
?>
