<?php
if(isset($_POST['id'])&&isset($_POST['name'])&&isset($_POST['email'])&& isset($_POST['fName'])&&isset($_POST['lName'])&&isset($_POST['phone']))
{
    //iclude functions
    require_once "conn.php";

    require_once "domain_exists.php";
    $stmt = mysqli_stmt_init($conn);
    //get the inputed data
    $id = $_POST['id'];
    $name = $_POST['name'];
    $fName = $_POST['fName'];
    $lName = $_POST['lName'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    if(isset($_POST['password'])){$password = $_POST['password'];
    $p = md5($password);
    
    $sql = "UPDATE users SET users.userFirstName = ?,users.userLastName = ?,users.userName = ?,users.userEmail =?,users.userPhone =?,users.userPassword=?  WHERE users.userID = ?";
    }
    else $sql ="UPDATE users SET users.userFirstName = ?,users.userLastName = ?,users.userName = ?,users.userEmail =?,users.userPhone =?  WHERE users.userID = ?";
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
        if ( mysqli_stmt_prepare($stmt,"SELECT * FROM users WHERE userEmail=? AND users.userID != ?")) { //sql injection protection
          // Bind parameters
          mysqli_stmt_bind_param($stmt,"ss", $email,$id);
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
          if(mysqli_stmt_prepare($stmt2,$sql))//insert query prepare
          {
            if(isset($_POST['password'])){mysqli_stmt_bind_param($stmt2,"sssssss",$fName,$lName,$name, $email,$phone,$p,$id);}
            else  mysqli_stmt_bind_param($stmt2,"ssssss",$fName,$lName,$name, $email,$phone,$id);
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