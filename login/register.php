<?php
if(isset($_POST['name'])&&isset($_POST['email'])&&isset($_POST['password']))
{
    //iclude functions
    require_once "conn.php";
    require_once "validate.php";
    require_once "domain_exists.php";
    //get the inputed date and validate them
    $name = validate($_POST['name']);
    $email = validate($_POST['email']);
    $password = validate($_POST['password']);
    //check if email was already created
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo("not_valid");
      }
      elseif(!domain_exists($email))
      {
        echo("not_valid");
      }
      else
      {
        $sql2 = "select * from users where email = '$email'";
        $result = $conn->query($sql2);
        if ($result->num_rows > 0) 
        {
            echo "User_exists";
        }
        //the query to add the new user, md5 for password security
        else
        {
            $sql = "insert into users values('','$name','$email', '" . md5($password) . "')";
            //check if the query was successful
            if(!$conn->query($sql)){
                echo "failure";
            }else{
                echo "success";   
            }
        }
      }
}
?>