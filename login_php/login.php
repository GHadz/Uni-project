<?php
if(isset($_POST["email"]) && isset($_POST["password"])){
    //include the other needed files
    require_once "conn.php";
    require_once "validate.php";
    //validate function to store the data properly
    $email = validate($_POST["email"]);
    $password = validate($_POST["password"]);
    //sql query and md5 to secure password then store results
    $sql = "select * from users where email = '$email'and password='". md5($password)."'";
    $result = $conn->query($sql);
//execute query and check if the password matches
    if($result->num_rows > 0){
        echo "success";
    }else {
        echo "failure";
    }

}
?>