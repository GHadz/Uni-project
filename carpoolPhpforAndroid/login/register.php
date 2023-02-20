<?php
if(isset($_POST['name'])&&isset($_POST['email'])&&isset($_POST['password']))
{
    //iclude functions
    require_once "conn.php";

    require_once "domain_exists.php";
    $stmt = mysqli_stmt_init($conn);
    //get the inputed data
    $name = $_POST['name'];
    $fName = $_POST['fName'];
    $lName = $_POST['lName'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $password = $_POST['password'];
    $p = md5($password);
    //basic email format check
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo("not_valid");
      }
      elseif(!domain_exists($email))//domain check
      {
        echo("not_valid");
      }


      else //check if email was already created
      {
        if ( mysqli_stmt_prepare($stmt,"SELECT * FROM users WHERE userEmail=?")) { //sql injection protection
          // Bind parameters
          mysqli_stmt_bind_param($stmt,"s", $email);
          // Execute query
           mysqli_stmt_execute($stmt);
          // Bind result variables
          $result = mysqli_stmt_get_result($stmt);
          // Close statement
          $stmt->close();
        }

        if ($result->num_rows > 0) 
        {
            echo "User_exists";
        }

        else
        {
          $stmt2 = mysqli_stmt_init($conn);//init
          if(mysqli_stmt_prepare($stmt2,"INSERT INTO users (userFirstName,userLastName,userName,userEmail,userPassword,userPhone) 
          VALUES (?,?,?,?,?,?)"))//insert query prepare
          {
          mysqli_stmt_bind_param($stmt2,"ssssss",$fName,$lName,$name, $email,$p,$phone); //bind variables
          $stmt2->execute();
            $result = mysqli_stmt_get_result($stmt2);
            
            //check if the query was successful
            if(mysqli_stmt_affected_rows($stmt2)>0){
                echo "success";  
            }else{
              echo "failure";
            }
            $stmt2->close();
          }
        }
      }
    }
?>